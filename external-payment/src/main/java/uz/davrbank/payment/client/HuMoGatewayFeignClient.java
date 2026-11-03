package uz.davrbank.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import uz.davrbank.payment.client.configuration.HuMoGatewayFeignConfiguration;
import uz.davrbank.payment.model.dto.huMo.request.*;
import uz.davrbank.payment.model.dto.huMo.response.HuMoBaseEmptyResponse;
import uz.davrbank.payment.model.dto.huMo.response.HuMoBasePaymentResponse;
import uz.davrbank.payment.model.dto.huMo.response.HuMoCreditResponse;

@FeignClient(name = "humo-gateway", url = "${external.humo-gateway.url}", configuration = HuMoGatewayFeignConfiguration.class)
public interface HuMoGatewayFeignClient {

    @PostMapping("/action/debitCreate")
    HuMoBasePaymentResponse debit(HuMoDebitRequest request);

    @PostMapping("/action/debitFinish")
    HuMoBasePaymentResponse debitConfirm(HuMoDebitConfirmRequest request);

    @PostMapping("/payment/byExternal")
    HuMoBasePaymentResponse getPaymentByExt(HuMoGetPaymentByExtRequest request);

    @PostMapping("/payment/byId")
    HuMoBasePaymentResponse getPaymentById(HuMoGetPaymentByIdRequest request);

    @PostMapping("/payment/returnByRefNum")
    HuMoBaseEmptyResponse returnByRefNum(HuMoReturnPaymentByExtRequest request);

    @PostMapping("/payment/returnById")
    HuMoBaseEmptyResponse returnById(HuMoReturnPaymentByIdRequest request);

    @PostMapping("/payment/cancelById")
    HuMoBaseEmptyResponse cancelById(HuMoCancelPaymentByIdRequest request);

    @PostMapping("/payment/cancelByRefNum")
    HuMoBaseEmptyResponse cancelByExt(HuMoCancelPaymentByExtRequest request);

    @PostMapping("/action/credit")
    HuMoCreditResponse credit(HuMoCreditRequest request);


}
