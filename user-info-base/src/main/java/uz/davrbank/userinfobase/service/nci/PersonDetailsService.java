package uz.davrbank.userinfobase.service.nci;

import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.userinfobase.dto.pagination.PaginationRequest;
import uz.davrbank.userinfobase.dto.pagination.PaginationResponse;
import uz.davrbank.userinfobase.dto.request.ResultResponse;
import uz.davrbank.userinfobase.dto.request.nci.LoanByPinflRequest;
import uz.davrbank.userinfobase.dto.request.nci.LoanInfoDto;
import uz.davrbank.userinfobase.dto.request.nci.LoanRequest;
import uz.davrbank.userinfobase.dto.request.nci.PersonDetailsRequest;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.nci.LoanByPinflResponse;
import uz.davrbank.userinfobase.dto.response.nci.LoanResponse;
import uz.davrbank.userinfobase.dto.response.nci.PersonDetailsResponse;

import java.util.List;



public interface PersonDetailsService {

    PersonDetailsResponse getUserDetails(PersonDetailsRequest request);

    LoanByPinflResponse getLoanByPinfl(LoanByPinflRequest request);

    ResultData<PaginationResponse<ResultResponse>> getAllUsers(String pinfl, String loanId, PaginationRequest paginationRequest);

    ResultData<ResultResponse> findById(Long id);

    ResultData<List<LoanInfoDto>> getUserLoans(String pinfl);

    LoanResponse getLoanInfo(LoanRequest request);

    List<PersonDetailsResponse> processPinflsFromCsv(MultipartFile file);

}
