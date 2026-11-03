package uz.davrbank.payment.service.web;

import uz.davrbank.payment.model.dto.uzcard.UzCardBaseRequest;
import uz.davrbank.payment.model.dto.uzcard.request.params.TransPayPurposeParam;
import uz.davrbank.payment.model.dto.uzcard.response.UzCardBaseResponse;
import uz.davrbank.payment.model.dto.uzcard.response.result.TransPayPurposeResult;
import uz.davrbank.payment.model.dto.uzcard.response.result.UzCardBasePaymentResult;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;

import java.util.List;

public interface UzCardIntegrationService {
    SimpleResponse<UzCardBaseResponse<TransPayPurposeResult>> pay(UzCardBaseRequest<TransPayPurposeParam> request);

    SimpleResponse<UzCardBaseResponse<List<UzCardBasePaymentResult>>> reverse(String refNum);

    SimpleResponse<UzCardBaseResponse<List<UzCardBasePaymentResult>>> getPaymentByRefNum(String refNum);

    SimpleResponse<UzCardBaseResponse<List<UzCardBasePaymentResult>>> getPaymentById(String paymentId);

    SimpleResponse<UzCardBaseResponse<?>> svGateConnect(UzCardBaseRequest<?> request);
}
