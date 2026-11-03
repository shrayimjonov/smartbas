package uz.davrbank.userinfobase.service.files.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;
import uz.davrbank.userinfobase.entity.files.FileUpload;
import uz.davrbank.userinfobase.entity.nci.LoanInfoEntity;
import uz.davrbank.userinfobase.enums.ResponseStatus;
import uz.davrbank.userinfobase.exceptions.FileException;
import uz.davrbank.userinfobase.properties.MinioProperties;
import uz.davrbank.userinfobase.repository.files.FileUploadRepository;
import uz.davrbank.userinfobase.repository.nci.LoanInfoRepository;
import uz.davrbank.userinfobase.service.FileStorageService;
import uz.davrbank.userinfobase.service.files.FileUploadService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final LoanInfoRepository loanInfoRepository;
    private final FileUploadRepository fileUploadRepository;
    private static final Tika tika = new Tika();
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final FileStorageService fileStorageService;

    @Override
    public ResultData<FileUploadResponse> uploadFile(MultipartFile file, String loanExtId) {
        if (file == null || file.isEmpty()) {
            return new ResultData<>(false, ResponseStatus.FAILED, "File is empty.");
        }
        LoanInfoEntity loanInfo = loanInfoRepository.findFirstByLoanExtId(loanExtId)
                .orElseThrow(() -> new FileException("Loan info not found: " + loanExtId));
        try {
            String fileName = file.getOriginalFilename();
            String objectName = fileStorageService.upload(file.getBytes(), fileName);
            FileUpload entity = FileUpload.builder()
                    .fileName(fileName)
                    .fileType(file.getContentType())
                    .minioObjectName(objectName)
                    .fileSize(file.getSize())
                    .uploadedAt(LocalDateTime.now())
                    .loanInfo(loanInfo)
                    .source("USER_UPLOAD")
                    .build();
            FileUpload saved = fileUploadRepository.save(entity);
            return new ResultData<>(FileUploadResponse.fromEntity(saved));
        } catch (Exception e) {
            log.error("File upload error", e);
            return new ResultData<>(false, ResponseStatus.FAILED, "File upload failed");
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> downloadFile(Long id) {

        FileUpload file = fileUploadRepository.findById(id)
                .orElseThrow(() -> new FileException("File not found"));

        try {
            byte[] content = fileStorageService.download(file.getMinioObjectName());
            String contentType = URLConnection.guessContentTypeFromName(file.getFileName());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(content);
        } catch (Exception e) {
            log.error("Download error", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResultData<List<FileUploadResponse>> getAllFiles(String loanExtId) {
        return getFilesInternal(loanExtId, null);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultData<List<FileUploadResponse>> getAllFilesV2(String loanExtId) {
        return getFilesInternal(loanExtId, "USER_UPLOAD");
    }

    private ResultData<List<FileUploadResponse>> getFilesInternal(String loanExtId, String source) {
        List<FileUpload> files;
        if (loanExtId == null || loanExtId.isBlank()) {
            files = (source == null)
                    ? fileUploadRepository.findAllByOrderByUploadedAtDesc()
                    : fileUploadRepository.findBySourceOrderByUploadedAtDesc(source);
        } else {
            files = (source == null)
                    ? fileUploadRepository.findByLoanInfo_LoanExtIdOrderByUploadedAtDesc(loanExtId)
                    : fileUploadRepository.findByLoanInfo_LoanExtIdAndSourceOrderByUploadedAtDesc(loanExtId, source);
        }

        List<FileUploadResponse> responses = files.stream()
                .map(FileUploadResponse::fromEntity)
                .toList();
        return new ResultData<>(responses);
    }


    @Override
    public ResultData<FileUploadResponse> uploadFileBase64(String loanExtId, String file) {
        if (file == null || file.isBlank()) {
            return new ResultData<>(false, ResponseStatus.FAILED, "File is empty.");
        }
        try {
            byte[] fileBytes = decodeBase64(file);
            String mimeType = tika.detect(fileBytes);
            String extension = getFileType(mimeType);
            String objectName = UUID.randomUUID() + "." + extension;
            uploadToMinio(fileBytes, objectName);
            FileUpload entity = FileUpload.builder()
                    .fileName(objectName)
                    .fileType(mimeType)
                    .minioObjectName(objectName)
                    .fileSize((long) fileBytes.length)
                    .uploadedAt(LocalDateTime.now())
                    .build();
            FileUpload saved = fileUploadRepository.save(entity);
            return new ResultData<>(FileUploadResponse.fromEntity(saved));
        } catch (Exception e) {
            log.error("Base64 upload error", e);
            return new ResultData<>(false, ResponseStatus.FAILED, "Invalid Base64 format.");
        }
    }

    @Override
    public ResultData<List<FileUploadResponse>> uploadFileBase64V2(
            String loanExtId,
            Integer remainingLoans,
            String file
    ) {
        if (file == null || file.isBlank()) {
            return new ResultData<>(false, ResponseStatus.FAILED, "File is empty.");
        }
        if (remainingLoans != 1) {
            return new ResultData<>(false, ResponseStatus.FAILED,
                    "File count does not match remainingLoans field. Expected: 1, Got: 1");
        }
        try {

            LoanInfoEntity loanInfo = loanInfoRepository.findFirstByLoanExtId(loanExtId)
                    .orElseThrow(() -> new FileException("Loan info not found: " + loanExtId));
            byte[] fileBytes = decodeBase64(file);
            String mimeType = tika.detect(fileBytes);
            String extension = getFileType(mimeType);
            String fileName = loanExtId + "." + extension;
            String objectName = UUID.randomUUID() + "." + extension;
            long fileSize = fileBytes.length;
            List<FileUploadResponse> uploadedFiles = new ArrayList<>();

            boolean exists = fileUploadRepository.existsByFileNameAndFileSize(fileName, fileSize);
            if (!exists) {
                uploadToMinio(fileBytes, objectName);
                FileUpload entity = FileUpload.builder()
                        .fileName(fileName)
                        .fileType(mimeType)
                        .minioObjectName(objectName)
                        .fileSize(fileSize)
                        .uploadedAt(LocalDateTime.now())
                        .source("ELMA")
                        .loanInfo(loanInfo)
                        .build();
                FileUpload saved = fileUploadRepository.save(entity);
                uploadedFiles.add(FileUploadResponse.fromEntity(saved));
            }

            return new ResultData<>(uploadedFiles);

        } catch (Exception e) {
            log.error("Base64 V2 upload error", e);
            return new ResultData<>(false, ResponseStatus.FAILED, "Invalid Base64 format.");
        }
    }

    @Override
    public ResultData<List<FileUploadResponse>> uploadFilesBase64V2(
            String loanExtId,
            Integer remainingLoans,
            List<String> files,
            String branchId
    ) {
        log.info("Upload files base64 v2: loanExtId: {}, branchId: {} remainingLoans: {}, file_size: {}", loanExtId, branchId, remainingLoans, files.size());
        if (files.isEmpty()) {
            return new ResultData<>(false, ResponseStatus.FAILED, "Files are empty.");
        }
        if (files.stream().anyMatch(file -> file == null || file.isBlank())) {
            return new ResultData<>(false, ResponseStatus.FAILED, "File is empty.");
        }

        try {
            LoanInfoEntity loanInfo = loanInfoRepository.findFirstByLoanExtIdAndBranchId(loanExtId, branchId)
                    .orElseGet(LoanInfoEntity::new);
            if (loanInfo.getLoanExtId() == null) {
                loanInfo.setLoanExtId(loanExtId);
                loanInfo.setBranchId(branchId);
                loanInfoRepository.save(loanInfo);
            }
            List<DecodedElmaFile> decodedFiles = files.stream()
                    .map(this::decodeElmaFile)
                    .toList();

            List<FileUploadResponse> uploadedFiles = new ArrayList<>();

            List<FileUpload> loanFiles = fileUploadRepository.findAllByLoanInfo_LoanExtIdAndLoanInfo_BranchIdAndSource(loanExtId, branchId, "ELMA");


            for (DecodedElmaFile decodedFile : decodedFiles) {
                Optional<FileUpload> optionalFile = fileUploadRepository.findFirstByLoanInfo_LoanExtIdAndLoanInfo_BranchIdAndFileNameAndFileSize(
                        loanExtId,
                        branchId,
                        decodedFile.fileName(),
                        decodedFile.fileSize()
                );
                if (optionalFile.isEmpty()) {
                    uploadToMinio(decodedFile.fileBytes(), decodedFile.objectName());
                    FileUpload entity = FileUpload.builder()
                            .fileName(decodedFile.fileName)
                            .fileType(decodedFile.mimeType())
                            .minioObjectName(decodedFile.objectName())
                            .fileSize(decodedFile.fileSize())
                            .uploadedAt(LocalDateTime.now())
                            .source("ELMA")
                            .loanInfo(loanInfo)
                            .build();
                    FileUpload saved = fileUploadRepository.save(entity);
                    uploadedFiles.add(FileUploadResponse.fromEntity(saved));
                } else {
                    loanFiles.remove(optionalFile.get());
                }
            }
            if (!loanFiles.isEmpty()) {
                fileUploadRepository.deleteAll(loanFiles);
                fileStorageService.deleteAsync(loanFiles.stream().map(FileUpload::getMinioObjectName).toList());
            }

            return new ResultData<>(uploadedFiles);

        } catch (Exception e) {
            log.error("Base64 V2 multi upload error", e);
            return new ResultData<>(false, ResponseStatus.FAILED, e.getMessage());
        }
    }

    private DecodedElmaFile decodeElmaFile(String file) {
        byte[] fileBytes = decodeBase64(file);
        String mimeType = tika.detect(fileBytes);
        String extension = getFileType(mimeType);
        String fileName = getFileName(file);
        return new DecodedElmaFile(
                fileBytes,
                mimeType,
                fileName,
                fileName + "." + extension
        );
    }

    private String getFileName(String base64) {
        byte[] fileBytes = decodeBase64(base64);
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.info("SHA-256 algorithm not found", e);
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(fileBytes);
        return HexFormat.of().formatHex(hash);
    }

    private record DecodedElmaFile(
            byte[] fileBytes,
            String mimeType,
            String fileName,
            String objectName
    ) {
        long fileSize() {
            return fileBytes.length;
        }
    }

    private void uploadToMinio(byte[] fileBytes, String objectName) throws Exception {
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .stream(is, fileBytes.length, -1)
                            .build()
            );
        }
    }

    private byte[] decodeBase64(String file) {
        if (file.contains(",")) {
            file = file.split(",")[1];
        }
        return Base64.getDecoder().decode(file);
    }

    public String getFileType(String mimeType) {
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType type = allTypes.forName(mimeType);
            return type.getExtension().replace(".", "");
        } catch (MimeTypeException e) {
            return "unknown";
        }
    }


    @SneakyThrows
    public void createBucket() {
        boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .build()
        );
        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        }
    }

    @SneakyThrows
    public void saveFile(InputStream inputStream, String file) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(file)
                .build());
    }

}
