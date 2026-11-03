package soap.humo_gateway.service;

import soap.humo_gateway.dto.request.*;

import javax.servlet.ServletRequest;

public interface PaymentService {
    Object getPaymentByPaymentId(ServletRequest servletRequest, PaymentRequest request);
    Object getPaymentByExternalId(ServletRequest servletRequest, RefNumRequest request);

    Object cancelActiveById(ServletRequest servletRequest, CancelRequest request);

    Object cancelActiveByRefNum(ServletRequest servletRequest, CancelRefRequest request);

    Object returnFinishedById(ServletRequest servletRequest, ReturnRequest request);
    Object returnFinishedByRefNum(ServletRequest servletRequest, ReturnRefRequest request);

    Object reconciliationCreate(ServletRequest servletRequest, RecoCreateRequest request);
    Object reconciliationFinish(ServletRequest servletRequest, RecoFinishRequest request);
}
