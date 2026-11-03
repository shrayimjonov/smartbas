package uz.davrbank.payment.service.web.impl;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.client.HuMoGatewayFeignClient;
import uz.davrbank.payment.model.dto.huMo.request.*;
import uz.davrbank.payment.model.dto.huMo.response.HuMoBaseEmptyResponse;
import uz.davrbank.payment.model.dto.huMo.response.HuMoBasePaymentResponse;
import uz.davrbank.payment.model.dto.huMo.response.HuMoCreditResponse;
import uz.davrbank.payment.service.web.HuMoGatewayService;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class HuMoGatewayServiceImpl implements HuMoGatewayService {
    private final HuMoGatewayFeignClient client;

    @Override
    public SimpleResponse<HuMoBasePaymentResponse> debit(HuMoDebitRequest request) {
        log.info("Debit request to huMo gate: {}", request);
        try {
            var response = client.debit(request);
            log.info("Debit response from huMo gate: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<HuMoBasePaymentResponse> debitConfirm(HuMoDebitConfirmRequest request) {
        log.info("Debit Confirm request to huMo gate: {}", request);
        try {
            var response = client.debitConfirm(request);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<HuMoBasePaymentResponse> getPaymentByExt(HuMoGetPaymentByExtRequest request) {
        log.info("Get Payment by externalID request to huMo gate: {}", request);
        try {
            var response = client.getPaymentByExt(request);
            log.info("Get Payment by externalID response from huMo gate: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<HuMoBasePaymentResponse> getPaymentById(HuMoGetPaymentByIdRequest request) {
        log.info("Get Payment by PaymentID request to huMo gate: {}", request);
        try {
            var response = client.getPaymentById(request);
            log.info("Get Payment by PaymentID response from huMo gate: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<HuMoBaseEmptyResponse> returnByRefNum(HuMoReturnPaymentByExtRequest request) {
        log.info("Return Payment by RefNum request to huMo gate: {}", request);
        try {
            var response = client.returnByRefNum(request);
            log.info("Return Payment by RefNum response from huMo gate: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<HuMoBaseEmptyResponse> returnById(HuMoReturnPaymentByIdRequest request) {
        log.info("Return Payment by PaymentID request to huMo gate: {}", request);
        try {
            var response = client.returnById(request);
            log.info("Return Payment by PaymentID response from huMo gate: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<HuMoBaseEmptyResponse> cancelByExt(HuMoCancelPaymentByExtRequest request) {
        log.info("Cancel Payment by RefNum request to huMo gate: {}", request);
        try {
            var response = client.cancelByExt(request);
            log.info("Cancel Payment by RefNum response from huMo gate: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<HuMoBaseEmptyResponse> cancelById(HuMoCancelPaymentByIdRequest request) {
        log.info("Cancel Payment by PaymentID request to huMo gate: {}", request);
        try {
            var response = client.cancelById(request);
            log.info("Cancel Payment by PaymentID response from huMo gate: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<HuMoCreditResponse> credit(HuMoCreditRequest request) {
        log.info("Sending credit method request to huMo gate: {}", request);
        try {
            var response = client.credit(request);
            log.info("Received credit method response from huMo gate: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            log.error("HuMo credit method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            log.error("HuMo credit method throws error: {}", f.getMessage());
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            log.error("HuMo credit method throws error: {}", e.getMessage());
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

}
