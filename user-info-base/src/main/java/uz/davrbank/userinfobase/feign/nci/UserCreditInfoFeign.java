package uz.davrbank.userinfobase.feign.nci;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import uz.davrbank.userinfobase.dto.request.nci.LoanByPinflRequest;
import uz.davrbank.userinfobase.dto.request.nci.LoanRequest;
import uz.davrbank.userinfobase.dto.request.nci.PersonDetailsRequest;
import uz.davrbank.userinfobase.dto.response.nci.LoanByPinflResponse;
import uz.davrbank.userinfobase.dto.response.nci.LoanResponse;
import uz.davrbank.userinfobase.dto.response.nci.PersonDetailsResponse;
import uz.davrbank.userinfobase.utils.Endpoints;


@FeignClient(
        name = "userInfoBaseFeignClient",
        url = "${loan.nci.url}",
        configuration = FeignUserInfoClientConfig.class
)

public interface UserCreditInfoFeign {

    @PostMapping(Endpoints.CLIENT_FULL_BY_PINFL)
    PersonDetailsResponse getUserDetails(PersonDetailsRequest request);

    @PostMapping(Endpoints.LOAN_INFO)
    LoanResponse getLoanInfo(LoanRequest request);

    @PostMapping(Endpoints.LOAN_BY_PINFL)
    LoanByPinflResponse getLoanByPinfl(LoanByPinflRequest request);


}

