package uz.davrbank.payment.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.davrbank.payment.client.configuration.BillingConfiguration;
import uz.davrbank.payment.model.dto.billing.request.CancelRequest;
import uz.davrbank.payment.model.dto.billing.request.CheckRequest;
import uz.davrbank.payment.model.dto.billing.request.HoldRequest;
import uz.davrbank.payment.model.dto.billing.request.PayRequest;
import uz.davrbank.payment.model.dto.billing.response.BillingBaseResponse;
import uz.davrbank.payment.model.dto.billing.response.BillingCancelHoldResponse;
import uz.davrbank.payment.model.dto.billing.response.result.DataResult;
import uz.davrbank.payment.model.dto.billing.response.result.SRData;

@FeignClient(value = "billing", url = "${external.billing.url}/services/bmms/api/agent", configuration = BillingConfiguration.class)
public interface BillingFeignClient {

    @PostMapping("/check/v3")
    BillingBaseResponse<Object> check(@RequestBody CheckRequest request);

    @PostMapping("/pay/v3")
    BillingBaseResponse<DataResult<SRData>> pay(@RequestBody PayRequest request);

    @PostMapping("/cancel/v3")
    BillingBaseResponse<Object> cancel(@RequestBody CancelRequest request);

    @PostMapping("/hold/v2")
    BillingBaseResponse<Object> holdActive(@RequestBody HoldRequest request);

    @PostMapping("/hold/v2/confirm/v2")
    BillingBaseResponse<DataResult<SRData>> holdConfirm(@RequestBody HoldRequest request);

    @GetMapping("/hold/v2/reverse-by-id/{id}")
    BillingCancelHoldResponse holdCancel(@PathVariable("id") Long id);


}
