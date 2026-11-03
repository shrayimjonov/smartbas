package soap.humo_gateway.service;


import soap.humo_gateway.dto.request.*;

import javax.servlet.ServletRequest;

public interface InfoService {
    Object humoHistory(ServletRequest servletRequest, HistoryRequest humoRequest);
    Object getCardTransitAccount(ServletRequest servletRequest, TransitRequest request);
    Object getCardBalance(ServletRequest servletRequest, BalanceRequest request);

    Object deActivateCard(ServletRequest servletRequest, DeactivateRequest request);
    Object activateCard(ServletRequest servletRequest, DeactivateRequest request);
    Object getPinCount(ServletRequest servletRequest, PinCountReq request);

    Object resetPinCount(ServletRequest servletRequest, ResetPinReq request);
    Object transferDataToFo(ServletRequest servletRequest, TransferDataRequest request);
}
