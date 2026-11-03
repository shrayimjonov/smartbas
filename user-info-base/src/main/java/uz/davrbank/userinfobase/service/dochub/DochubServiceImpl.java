package uz.davrbank.userinfobase.service.dochub;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.userinfobase.dto.request.dochub.DochubRequest;
import uz.davrbank.userinfobase.dto.request.dochub.LoanIdRequestDto;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.dochub.DochubResponse;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;
import uz.davrbank.userinfobase.entity.files.FileUpload;
import uz.davrbank.userinfobase.entity.nci.LoanInfoEntity;
import uz.davrbank.userinfobase.enums.ResponseStatus;
import uz.davrbank.userinfobase.exceptions.DochubException;
import uz.davrbank.userinfobase.feign.dochub.DochubTransferFeign;
import uz.davrbank.userinfobase.repository.files.FileUploadRepository;
import uz.davrbank.userinfobase.repository.nci.LoanInfoRepository;
import uz.davrbank.userinfobase.service.files.impl.FileUploadServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class DochubServiceImpl implements DochubService {
    private final DochubTransferFeign dochubFeignClient;
    private final FileUploadServiceImpl fileUploadService;
    private final FileUploadRepository fileUploadRepository;
    private final LoanInfoRepository loanInfoRepository;

    @Override
    public ResultData<FileUploadResponse> requestToDochub(LoanIdRequestDto requestDto) {
        DochubRequest dochubRequest = buildDochubRequest(requestDto.getLoanId());
        DochubResponse response;
        try {
            response = dochubFeignClient.getDochubTransfer(dochubRequest);
        } catch (FeignException e) {
            log.error("DOCHUB API xatolik qaytardi: status={}, body={}",
                    e.status(), e.contentUTF8());
            throw new DochubException("DOCHUB servisidan xatolik qaytdi: " + e.contentUTF8());
        } catch (Exception e) {
            log.error("DOCHUB bilan bog‘lanishda xatolik", e);
            throw new DochubException("DOCHUB servisiga ulanishda xatolik yuz berdi.");
        }
            if (response == null || response.getResult() == null || response.getResult().getFile_data() == null) {
            throw new DochubException("Fayl topilmadi yoki noto‘g‘ri javob: natija mavjud emas.");
        }
        try {
            byte[] fileBytes = Base64.getDecoder().decode(response.getResult().getFile_data());
            String fileName = requestDto.getLoanId() + ".pdf";
            Long fileSize = (long) fileBytes.length;
            boolean exists = fileUploadRepository.existsByFileNameAndFileSize(fileName, fileSize);

            if (exists) {
                log.info("DOCHUB fayl '{}' ({} bytes) allaqachon mavjud. Yuklanmaydi.", fileName, fileSize);
                return new ResultData<>(false, ResponseStatus.FAILED, "Fayl allaqachon mavjud. Saqlanmadi.");
            }

            Tika tika = new Tika();
            String mimeType = tika.detect(fileBytes);

            String fileType = fileUploadService.getFileType(mimeType);
            String minioObjectName = UUID.randomUUID() + "_" + fileName;

            fileUploadService.createBucket();
            try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
                fileUploadService.saveFile(inputStream, minioObjectName);
            }
            LoanInfoEntity loanInfo = loanInfoRepository
                    .findFirstByLoanId(requestDto.getLoanId())
                    .orElseThrow(() -> new DochubException("LoanInfo topilmadi: loanId = " + requestDto.getLoanId()));
            FileUpload fileUpload = FileUpload.builder()
                    .fileName(fileName)
                    .fileSize((long) fileBytes.length)
                    .fileType(fileType)
                    .minioObjectName(minioObjectName)
                    .loanInfo(loanInfo)
                    .source("DOCHUB")
                    .uploadedAt(LocalDateTime.now())
                    .build();
            FileUpload savedFileUpload = fileUploadRepository.save(fileUpload);
            FileUploadResponse fileUploadResponse = FileUploadResponse.fromEntity(savedFileUpload);
            return new ResultData<>(fileUploadResponse);
        }  catch (IllegalArgumentException | IOException e) {
            return new ResultData<>(false, ResponseStatus.FAILED, "Invalid Base64 format.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResultData<List<FileUploadResponse>> getDochubFilesByLoanId(String loanId) {
        List<FileUpload> files = fileUploadRepository
                .findByLoanInfo_LoanIdAndSourceOrderByUploadedAtDesc(loanId, "DOCHUB");

        List<FileUploadResponse> responseList = files.stream()
                .map(FileUploadResponse::fromEntity)
                .collect(Collectors.toList());

        return new ResultData<>(responseList);
    }


    private DochubRequest buildDochubRequest(String loanId) {
        DochubRequest.Params params = DochubRequest.Params.builder()
                .loanId(loanId)
                .file_name("application.pdf")
                .build();

        return DochubRequest.builder()
                .jsonRpc("2.0")
                .method("loadFile")
                .service("kk_dochub")
                .params(params)
                .build();
    }


}
