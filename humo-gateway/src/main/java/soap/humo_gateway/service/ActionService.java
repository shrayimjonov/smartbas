package soap.humo_gateway.service;

import soap.humo_gateway.dto.request.*;

import javax.servlet.ServletRequest;

public interface ActionService {

    Object p2p(ServletRequest servletRequest, P2pRequest request);

    Object debitCreate(ServletRequest servletRequest, DebitCreateRequest request);

    Object debitConfirm(ServletRequest servletRequest, DebitConfirmRequest request);

    Object credit(ServletRequest servletRequest, CreditRequest request);

    Object executeTransaction(ServletRequest servletRequest, ExecuteTransactionRequest request);

    Object pinAssign(ServletRequest servletRequest, PinAssignRequest request);



}
