package uz.davrbank.payment.service.web.impl;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.client.UzCardIntegrationFeignClient;
import uz.davrbank.payment.model.dto.uzcard.UzCardBaseRequest;
import uz.davrbank.payment.model.dto.uzcard.request.params.TransPayPurposeParam;
import uz.davrbank.payment.model.dto.uzcard.response.UzCardBaseResponse;
import uz.davrbank.payment.model.dto.uzcard.response.result.TransPayPurposeResult;
import uz.davrbank.payment.model.dto.uzcard.response.result.UzCardBasePaymentResult;
import uz.davrbank.payment.service.web.UzCardIntegrationService;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UzCardIntegrationServiceImpl implements UzCardIntegrationService {
    private final UzCardIntegrationFeignClient client;


    @Override
    public SimpleResponse<UzCardBaseResponse<TransPayPurposeResult>> pay(UzCardBaseRequest<TransPayPurposeParam> request) {
        log.info("Pay request to uzcard_integration: {}", request);
        try {
            var response = client.transPayPurpose(request);
            log.info("Pay response from uzcard_integration: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            log.error("SVGate trans pay method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<UzCardBaseResponse<List<UzCardBasePaymentResult>>> reverse(String refNum) {
        log.info("Reverse request to uzcard_integration: {}", refNum);
        try {
            var response = client.reverse(refNum);
            log.info("Reverse response from uzcard_integration: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            log.error("SVGate reverse method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<UzCardBaseResponse<List<UzCardBasePaymentResult>>> getPaymentByRefNum(String refNum) {
        log.info("Get payment by Ref Num request to uzcard_integration: {}", refNum);
        try {
            var response = client.getPaymentByRefNum(refNum);
            log.info("Get payment by Ref Num response from uzcard_integration: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            log.error("SVGate get payment by refNum method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<UzCardBaseResponse<List<UzCardBasePaymentResult>>> getPaymentById(String paymentId) {
        log.info("Get payment by PaymentID request to uzcard_integration: {}", paymentId);
        try {
            var response = client.getPaymentById(paymentId);
            log.info("Get payment by PaymentID response from uzcard_integration: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            log.error("SVGate get payment byId method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<UzCardBaseResponse<?>> svGateConnect(UzCardBaseRequest<?> request) {
        log.info("Send universal method request to svGate: {}", request);
        try {
            var response = client.svGateConnect(request);
            log.info("Received universal method response from svGate: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            log.error("SVGate universal method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (FeignException f) {
            log.error("SVGate universal method throws error: {}", f.getMessage());
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            log.error("SVGate universal method throws error: {}", e.getMessage());
            return new SimpleResponse<>(582, e.getMessage());
        }
    }

}
