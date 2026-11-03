package uz.davrbank.payment.service.web;

import uz.davrbank.payment.model.dto.billing.request.CancelRequest;
import uz.davrbank.payment.model.dto.billing.request.CheckRequest;
import uz.davrbank.payment.model.dto.billing.request.HoldRequest;
import uz.davrbank.payment.model.dto.billing.request.PayRequest;
import uz.davrbank.payment.model.dto.billing.response.BillingBaseResponse;
import uz.davrbank.payment.model.dto.billing.response.BillingCancelHoldResponse;
import uz.davrbank.payment.model.dto.billing.response.result.DataResult;
import uz.davrbank.payment.model.dto.billing.response.result.SRData;

public interface BillingClientService {
    BillingBaseResponse<Object> check(CheckRequest request);

    BillingBaseResponse<DataResult<SRData>> pay(PayRequest request);

    BillingBaseResponse<Object> cancel(CancelRequest request);

    BillingBaseResponse<Object> holdActive(HoldRequest request);

    BillingBaseResponse<DataResult<SRData>> holdConfirm(HoldRequest request);

    BillingCancelHoldResponse holdCancel(Long id);
}
