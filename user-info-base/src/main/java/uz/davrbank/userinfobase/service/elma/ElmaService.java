package uz.davrbank.userinfobase.service.elma;

import uz.davrbank.userinfobase.dto.request.elma.ElmaResponseDto;
import uz.davrbank.userinfobase.dto.request.elma.LoanIdAndBranchIdRequestDto;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;

import java.util.List;


public interface ElmaService {
    ResultData<ElmaResponseDto> requestToElma(LoanIdAndBranchIdRequestDto requestDto);
    ResultData<List<FileUploadResponse>> getAllElmaFilesByLoanExtId(String loanExtId);
}
