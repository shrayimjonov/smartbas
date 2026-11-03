package uz.davrbank.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.payment.client.configuration.UzCardIntegrationFeignConfiguration;
import uz.davrbank.payment.model.dto.uzcard.UzCardBaseRequest;
import uz.davrbank.payment.model.dto.uzcard.request.params.TransPayPurposeParam;
import uz.davrbank.payment.model.dto.uzcard.response.UzCardBaseResponse;
import uz.davrbank.payment.model.dto.uzcard.response.result.TransPayPurposeResult;
import uz.davrbank.payment.model.dto.uzcard.response.result.UzCardBasePaymentResult;

import java.util.List;

@FeignClient(value = "uzcard-integration", url = "${external.uzcard.url}", configuration = UzCardIntegrationFeignConfiguration.class)
public interface UzCardIntegrationFeignClient {

    @PostMapping("/transPayPurpose")
    UzCardBaseResponse<TransPayPurposeResult> transPayPurpose(UzCardBaseRequest<TransPayPurposeParam> request);

    @PostMapping("/reverseExtSv")
    UzCardBaseResponse<List<UzCardBasePaymentResult>> reverse(@RequestParam("tranId") String transactionId);

    @GetMapping("/extsv")
    UzCardBaseResponse<List<UzCardBasePaymentResult>> getPaymentById(@RequestParam("sv") String utrnno);

    @GetMapping("/extsv")
    UzCardBaseResponse<List<UzCardBasePaymentResult>> getPaymentByRefNum(@RequestParam("ext") String refNum);

    @PostMapping("/adm/v1/svGate")
    UzCardBaseResponse<?> svGateConnect(UzCardBaseRequest<?> request);

}
