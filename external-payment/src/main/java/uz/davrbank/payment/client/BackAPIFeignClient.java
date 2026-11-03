package uz.davrbank.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import uz.davrbank.payment.client.configuration.BackApiClientConfiguration;
import uz.davrbank.payment.model.dto.backAPI.request.*;
import uz.davrbank.payment.model.dto.backAPI.response.BackCheckTranResponse;
import uz.davrbank.payment.model.dto.backAPI.response.BackForexResponse;
import uz.davrbank.payment.model.dto.backAPI.response.BackSimpleTranResponse;
import uz.davrbank.payment.model.dto.backAPI.response.BackVFXResponse;

import java.util.List;

@FeignClient(name = "back-api", url = "${external.back-api.url}",
        path = "/back/v2",
        configuration = BackApiClientConfiguration.class)
public interface BackAPIFeignClient {

    @PostMapping("/InterbankOperation")
    List<BackSimpleTranResponse> interBranch(@RequestHeader("branch") String schema, BackInterBranchRequest request);

    @PostMapping("/FX_Operation")
    List<BackForexResponse> conversion(@RequestHeader("branch") String schema, BackForexRequest request);

    @PostMapping("/bCreateDoc")
    List<BackSimpleTranResponse> doSimpleTransaction(@RequestHeader("branch") String schema, BackSimpleTransactionRequest request);

    @PostMapping("/VFX_OPERS_BUY")
    List<BackVFXResponse> vfxOperation(@RequestHeader("branch") String schema, BackVFXRequest request);

    @PostMapping("/getBSReqId")
    List<BackCheckTranResponse> getTransactionById(@RequestHeader("branch") String schema, BackCheckTranRequest request);


}
