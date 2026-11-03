package uz.davrbank.payment.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import uz.davrbank.payment.client.configuration.ABSHubFeignConfiguration;
import uz.davrbank.payment.model.dto.absHub.request.ABSTransactionSimpleRequest;
import uz.davrbank.payment.model.dto.absHub.request.AbsHubTransactionReverseRequest;
import uz.davrbank.payment.model.dto.absHub.response.ABSTransactionSimpleResponse;
import uz.davrbank.payment.model.dto.absHub.response.AbsHubTransactionReverseResponse;
import uz.nodir.commondto.model.dto.core.response.ResultData;


@FeignClient(name = "absHub", url = "${external.abs-hub.url}", path = "/hub", configuration = ABSHubFeignConfiguration.class)
public interface ABSHubFeignClient {
    @GetMapping("/api/v1/general/reverse")
    ResultData<AbsHubTransactionReverseResponse> reverse(@Valid AbsHubTransactionReverseRequest request);

    @PostMapping("api/v1/transaction/simple")
    ResultData<ABSTransactionSimpleResponse> simpleTransaction(ABSTransactionSimpleRequest request);


}
