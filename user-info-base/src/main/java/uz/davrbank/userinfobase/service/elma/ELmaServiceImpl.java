package uz.davrbank.userinfobase.service.elma;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.userinfobase.dto.request.elma.ElmaRequestDto;
import uz.davrbank.userinfobase.dto.request.elma.ElmaResponseDto;
import uz.davrbank.userinfobase.dto.request.elma.LoanIdAndBranchIdRequestDto;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;
import uz.davrbank.userinfobase.entity.files.FileUpload;
import uz.davrbank.userinfobase.exceptions.ElmaException;
import uz.davrbank.userinfobase.feign.elma.ElmaFeignClient;
import uz.davrbank.userinfobase.repository.files.FileUploadRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ELmaServiceImpl implements ElmaService {
    private final ElmaFeignClient feignClient;
    private final FileUploadRepository fileUploadRepository;

    @Override
    public ResultData<ElmaResponseDto> requestToElma(LoanIdAndBranchIdRequestDto requestDto) {
        ElmaRequestDto elmaRequestDto =
                buildElmaRequest(requestDto.getLoanId(), requestDto.getBranchId());
        try {
            log.info("ELMA request for loanExtID: {}",requestDto.getLoanId());
            ElmaResponseDto elmaResponseDto = feignClient.transfer(elmaRequestDto);
            if (elmaResponseDto == null) {
                throw new ElmaException("ELMA javobi bo‘sh qaytdi.");
            }
            log.info("ELMA response {}", elmaResponseDto);
            return new ResultData<>(elmaResponseDto);
        } catch (FeignException e) {
            log.error("ELMA API xatolik qaytardi: status={}, body={}",
                    e.status(), e.contentUTF8());
            throw new ElmaException("ELMA servisidan xatolik qaytdi: " + e.contentUTF8());
        } catch (Exception e) {
            log.error("ELMA bilan bog‘lanishda xatolik yuz berdi", e);
            throw new ElmaException("ELMA servisiga ulanishda xatolik yuz berdi.");
        }
    }

    @Override
    public ResultData<List<FileUploadResponse>> getAllElmaFilesByLoanExtId(String loanExtId) {
        List<FileUpload> elmaFiles = fileUploadRepository
                .findAllBySourceAndLoanInfo_LoanExtIdOrderByUploadedAtDesc("ELMA", loanExtId);
        List<FileUploadResponse> responses = elmaFiles.stream()
                .map(FileUploadResponse::fromEntity)
                .toList();
        return new ResultData<>(responses);
    }
    private ElmaRequestDto buildElmaRequest(String loanId, String branchId) {
        ElmaRequestDto.MessageContentDto dto = ElmaRequestDto.MessageContentDto
                .builder()
                .LoanId(loanId)
                .branchId(branchId)
                .build();

        return ElmaRequestDto
                .builder()
                .action("getLoanDoc")
                .method("getFile")
                .server("prod")
                .Success("1")
                .MessageType("DataResponse")
                .RetryRequestId("")
                .TransactionRequestID("")
                .ErrorMessage("")
                .InstanceID("")
                .messageContent(dto)
                .build();
    }
}
