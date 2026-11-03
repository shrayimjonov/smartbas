package nci.billing.feign;

import nci.billing.model.request.CheckTransactionRequest;
import nci.billing.model.request.PayRequest;
import nci.billing.model.response.CheckResponse;
import nci.billing.model.response.PayResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "isLoan", url = "${nci.isLoan-service.url}", configuration = IsLoanConfiguration.class)
public interface IsLoanFeign {

    @PostMapping("/isloan/CheckTransaction")
    CheckResponse getClientDataByLoanId(@RequestBody CheckTransactionRequest request);

    @PostMapping("/isloan/payment")
    PayResponse doTransaction(@RequestBody PayRequest request);


}
