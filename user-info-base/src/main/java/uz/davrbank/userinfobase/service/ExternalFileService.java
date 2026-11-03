package uz.davrbank.userinfobase.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.userinfobase.dto.ExternalFileDtoResponse;
import uz.davrbank.userinfobase.dto.ExternalFileRequestDto;
import uz.davrbank.userinfobase.dto.request.DavrhubFileListRequest;
import uz.davrbank.userinfobase.dto.request.dochub.LoanIdRequestDto;
import uz.davrbank.userinfobase.dto.request.elma.LoanIdAndBranchIdRequestDto;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.entity.ExternalFile;
import uz.davrbank.userinfobase.entity.nci.LoanInfoEntity;
import uz.davrbank.userinfobase.enums.SystemSource;
import uz.davrbank.userinfobase.exceptions.GeneralException;
import uz.davrbank.userinfobase.repository.ExternalFileRepository;
import uz.davrbank.userinfobase.repository.files.FileUploadRepository;
import uz.davrbank.userinfobase.repository.nci.LoanInfoRepository;
import uz.davrbank.userinfobase.service.dochub.DochubService;
import uz.davrbank.userinfobase.service.elma.ElmaService;

import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static uz.davrbank.userinfobase.service.DavrhubService.getMimeType;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalFileService {

    private final DavrhubService davrhubService;
    private final DochubService dochubService;
    private final ElmaService elmaService;

    private final ExternalFileRepository externalFileRepository;
    private final FileUploadRepository fileUploadRepository;
    private final LoanInfoRepository loanInfoRepository;
    private final HashIdService hashIdService;
    private final FileStorageService fileStorageService;

    private static final Set<SystemSource> MIRRORED_SOURCES = EnumSet.of(
            SystemSource.DOCHUB,
            SystemSource.ELMA,
            SystemSource.CRM
    );
    private static final Set<String> MIRRORED_UPLOAD_SOURCE_NAMES = MIRRORED_SOURCES.stream()
            .map(Enum::name)
            .collect(Collectors.toUnmodifiableSet());

    /**
     * NCI, DOCHUB va ELMA tizimlaridan fayllarni sinxronlash
     */

    public ResultData<List<ExternalFileDtoResponse>> syncAllFiles(ExternalFileRequestDto request) {

        String loanId = requireNumericText(request == null ? null : request.getLoanId(), "loanId");
        String branchId = requireText(request == null ? null : request.getBranchId(), "branchId");

        log.info("SYNC START → loanId={}, branchId={}", loanId, branchId);

        safeRun(() -> syncElma(loanId, branchId), "ELMA");
        safeRun(() -> syncDavrhub(loanId, branchId), "NCI");
//        safeRun(() -> syncDochub(loanId), "DOCHUB");

        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            log.warn("Sleep interrupted");
        }
        safeRun(() -> mirrorFileUploadsToExternal(loanId, branchId), "MIRROR");

        List<ExternalFileDtoResponse> response = loadExternalFileResponses(loanId, branchId);

        log.info("SYNC FINISHED → total files={}", response.size());

        return new ResultData<>(response);
    }

    private String requireNumericText(String value, String fieldName) {
        String text = requireText(value, fieldName);
        if (!text.matches("\\d+")) {
            throw new GeneralException(fieldName + " must contain only digits");
        }
        return text;
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new GeneralException(fieldName + " is required");
        }
        return value.trim();
    }

    private void safeRun(Runnable action, String systemName) {
        try {
            action.run();
            log.info("{} SUCCESS", systemName);
        } catch (FeignException e) {
            log.warn("{} FEIGN ERROR (skipped): {}", systemName, e.contentUTF8());
        } catch (Exception e) {
            log.warn("{} GENERAL ERROR (skipped): {}", systemName, e.getMessage());
        }
    }


    /**
     * NCI (Davrhub)
     */
    private void syncDavrhub(String loanId, String branchId) {
        DavrhubFileListRequest req = new DavrhubFileListRequest(loanId, branchId);
        davrhubService.syncFiles(req);
        int backfilled = davrhubService.backfillExternalFiles(req);
        if (backfilled > 0) {
            log.info("NCI EXTERNAL FILES BACKFILLED → loanId={}, backfilledFiles={}", loanId, backfilled);
        }
    }

    /**
     * DOCHUB
     */
    private void syncDochub(String loanId) {
        dochubService.requestToDochub(
                LoanIdRequestDto.builder()
                        .loanId(loanId)
                        .build()
        );
    }

    /**
     * ELMA
     */
    private void syncElma(String loanId, String branchId) {
        elmaService.requestToElma(
                LoanIdAndBranchIdRequestDto.builder()
                        .LoanId(loanId)
                        .branchId(branchId)
                        .build()
        );
    }

    /**
     * file_uploads (ELMA / DOCHUB) → external_files ga kochirish
     */
    private void mirrorFileUploadsToExternal(String loanExtId, String branchId) {

        List<FileUploadRepository.MirrorCandidateView> uploads =
                findUploadsWithRetry(loanExtId, branchId);

        if (uploads.isEmpty()) {
            return;
        }

        Set<SystemSource> sources = uploads.stream()
                .map(FileUploadRepository.MirrorCandidateView::getSource)
                .map(this::resolveSystemSource)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(SystemSource.class)));

        if (sources.isEmpty()) {
            return;
        }

        Set<String> uploadIds = uploads.stream()
                .map(FileUploadRepository.MirrorCandidateView::getId)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.toSet());

        if (uploadIds.isEmpty()) {
            return;
        }

        Set<String> existingKeys = externalFileRepository
                .findKeysBySourceInAndExternalFileIdIn(sources, uploadIds).stream()
                .map(view -> toExternalKey(view.getSource(), view.getExternalFileId()))
                .collect(Collectors.toSet());

        List<ExternalFile> newExternalFiles = uploads.stream()
                .map(upload -> toExternalFile(upload, loanExtId, branchId))
                .filter(Objects::nonNull)
                .filter(file -> !existingKeys.contains(toExternalKey(file.getSource(), file.getExternalFileId())))
                .toList();

        if (!newExternalFiles.isEmpty()) {
            externalFileRepository.saveAll(newExternalFiles);
        }

        log.info(
                "MIRROR FINISHED → loanId={}, loanExtId={}, scanned={}, newFiles={}",
                loanExtId,
                loanExtId,
                uploads.size(),
                newExternalFiles.size()
        );
    }

    private List<FileUploadRepository.MirrorCandidateView> findUploadsWithRetry(
            String loanExtId, String branchId) {
        int[] delays = {5, 10, 10};
        int retryCount = 0;

        while (retryCount <= delays.length) {
            List<FileUploadRepository.MirrorCandidateView> uploads = fileUploadRepository
                    .findAllBySourceInAndLoanInfo_LoanExtIdAndLoanInfo_BranchId(MIRRORED_UPLOAD_SOURCE_NAMES, loanExtId, branchId);

            if (!uploads.isEmpty()) {
                if (retryCount > 0) {
                    log.info("Uploads found after {} retries for loanId={}", retryCount, loanExtId);
                }
                return uploads;
            }

            if (retryCount < delays.length) {
                int delaySeconds = delays[retryCount];
                log.debug("No uploads found, retrying after {} seconds for loanId={}", delaySeconds, loanExtId);
                try {
                    Thread.sleep(delaySeconds * 1000L);
                } catch (InterruptedException e) {
                    log.warn("Retry sleep interrupted for loanId={}", loanExtId);
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            retryCount++;
        }

        log.debug("No uploads found after all retries for loanId={}", loanExtId);
        return new ArrayList<>();
    }

    private List<ExternalFileDtoResponse> loadExternalFileResponses(String loanId, String branchId) {

        List<ExternalFileDtoResponse> nciResponses = davrhubService.getPersistedFiles(loanId).stream()
                .map(file -> ExternalFileDtoResponse.fromDavrhubFile(file, hashIdService))
                .toList();
        List<ExternalFileDtoResponse> responses = new ArrayList<>(nciResponses);

        AtomicInteger counter = new AtomicInteger();
        List<ExternalFileDtoResponse> mirroredResponses = externalFileRepository.findAllByLoanIdAndBranchId(loanId, branchId).stream()
                .filter(file -> file.getSource() != SystemSource.NCI)
                .map(file -> ExternalFileDtoResponse.fromExternalFile(file, hashIdService, counter.incrementAndGet()))
                .toList();
        responses.addAll(mirroredResponses);

        log.info(
                "SYNC RESPONSE BUILD → loanId={}, nciFiles={}, mirroredFiles={}, totalFiles={}",
                loanId,
                nciResponses.size(),
                mirroredResponses.size(),
                responses.size()
        );

        return responses;
    }

    private ExternalFile toExternalFile(
            FileUploadRepository.MirrorCandidateView upload,
            String loanId,
            String branchId
    ) {
        if (upload.getId() == null) {
            return null;
        }

        SystemSource source = resolveSystemSource(upload.getSource());
        if (source == null) {
            return null;
        }

        return ExternalFile.builder()
                .source(source)
                .loanId(loanId)
                .branchId(branchId)
                .fileName(upload.getFileName())
                .nameDoc(upload.getFileName())
                .externalFileId(upload.getId().toString())
                .fileSize(upload.getFileSize())
                .minioObjectName(upload.getMinioObjectName())
                .mimeType(upload.getFileType())
                .build();
    }

    private String resolveLoanExtId(String loanId, String branchId) {
        if (loanId == null || loanId.isBlank()) {
            return null;
        }
        return loanInfoRepository.findFirstByLoanId(loanId)
                .map(LoanInfoEntity::getLoanExtId)
                .filter(id -> !id.isBlank())
                .orElseGet(() -> {
                    if (branchId == null || branchId.isBlank()) {
                        return null;
                    }
                    return branchId + loanId;
                });
    }

    private SystemSource resolveSystemSource(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        try {
            return SystemSource.valueOf(source);
        } catch (IllegalArgumentException e) {
            log.debug("Skipping unsupported upload source during external sync: {}", source);
            return null;
        }
    }

    private String toExternalKey(SystemSource source, String externalFileId) {
        return source.name() + ":" + externalFileId;
    }

    /**
     * Download file
     */
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> downloadFile(String hashId) {

        log.info("DOWNLOAD REQUEST → hashId={}", hashId);

        Long id = hashIdService.decode(hashId);

        ExternalFile file = externalFileRepository.findById(id)
                .orElseThrow(() -> new GeneralException("File not found"));

        if (file.getMinioObjectName() == null) {
            throw new GeneralException("File not stored in MinIO");
        }

        byte[] fileContent = fileStorageService.download(file.getMinioObjectName());

        String mimeType = URLConnection.guessContentTypeFromName(file.getFileName());
        mimeType = getMimeType(mimeType);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\""
                )
                .body(fileContent);
    }


    @Transactional
    public void deleteFiles() {
        externalFileRepository.deleteAll();
        fileUploadRepository.flush();
        log.info("EXTERNAL FILES DELETED");
    }

    @Transactional
    public void deleteFileUploads() {
        fileUploadRepository.deleteAll();
        fileUploadRepository.flush();
        log.info(" FILE UPLOADS DELETED");
    }
}
