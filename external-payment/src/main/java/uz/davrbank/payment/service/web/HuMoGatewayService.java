package uz.davrbank.payment.service.web;


import uz.davrbank.payment.model.dto.huMo.request.*;
import uz.davrbank.payment.model.dto.huMo.response.HuMoBaseEmptyResponse;
import uz.davrbank.payment.model.dto.huMo.response.HuMoBasePaymentResponse;
import uz.davrbank.payment.model.dto.huMo.response.HuMoCreditResponse;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;

public interface HuMoGatewayService {
    SimpleResponse<HuMoBasePaymentResponse> debit(HuMoDebitRequest request);

    SimpleResponse<HuMoBasePaymentResponse> debitConfirm(HuMoDebitConfirmRequest request);

    SimpleResponse<HuMoBasePaymentResponse> getPaymentByExt(HuMoGetPaymentByExtRequest request);

    SimpleResponse<HuMoBasePaymentResponse> getPaymentById(HuMoGetPaymentByIdRequest request);

    SimpleResponse<HuMoBaseEmptyResponse> returnByRefNum(HuMoReturnPaymentByExtRequest request);

    SimpleResponse<HuMoBaseEmptyResponse> returnById(HuMoReturnPaymentByIdRequest request);

    SimpleResponse<HuMoBaseEmptyResponse> cancelByExt(HuMoCancelPaymentByExtRequest request);

    SimpleResponse<HuMoBaseEmptyResponse> cancelById(HuMoCancelPaymentByIdRequest request);

    SimpleResponse<HuMoCreditResponse> credit(HuMoCreditRequest request);
}
