package uz.davrbank.userinfobase.service.dochub;


import uz.davrbank.userinfobase.dto.request.dochub.LoanIdRequestDto;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;

import java.util.List;

public interface DochubService {
    ResultData<FileUploadResponse> requestToDochub(LoanIdRequestDto requestDto);
    ResultData<List<FileUploadResponse>> getDochubFilesByLoanId(String loanId);
}
