package nci.billing.feign;

import jakarta.validation.Valid;
import nci.billing.model.request.loan.PaymentRequest;
import nci.billing.model.response.loan.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "loan", url = "${nci.loan-api.url}/loan/overdues", configuration = LoanFeignConfiguration.class)
public interface LoanFeign {

    @GetMapping("/{PINFL}")
    LoanInfoByPINFLResponse getLoanInfoByPINFL(@PathVariable("PINFL") String PINFL);

    @GetMapping("/{branch}/{loanId}")
    LoanInfoResponse getLoanInfo(@PathVariable("branch") String branch,
                                 @PathVariable("loanId") Integer loanId);

    @GetMapping("/details/{branch}/{loanId}")
    LoanInfoDetailedResponse getLoanDetailedInfo(@PathVariable("branch") String branch,
                                                 @PathVariable("loanId") Integer loanId);

    @GetMapping("/async/{branch}")
    DebtorListTokenResponse getTokenForTakeDebtorsList(@PathVariable("branch") String branch);

    @GetMapping("/status/{token}")
    DebtorListFullResponse getDebtorsList(@PathVariable("token") String token);

    @PostMapping("/async/payment")
    DoPaymentResponse doPayment(@RequestBody @Valid PaymentRequest request);

    @GetMapping("/status/payment/{paymentId}")
    GetPaymentResponse getPaymentStatus(@PathVariable("paymentId") String paymentId);


}
