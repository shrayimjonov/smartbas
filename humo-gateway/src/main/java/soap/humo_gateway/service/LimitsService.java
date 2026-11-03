package soap.humo_gateway.service;

import soap.humo_gateway.dto.request.limits.GetCardLimitBalanceRequest;
import soap.humo_gateway.dto.request.limits.GetCardLimitRequest;
import soap.humo_gateway.dto.request.limits.GetLimits;
import soap.humo_gateway.dto.request.limits.SetCardLimitRequest;
import soap.humo_gateway.dto.response.LimitListResponse;

import javax.servlet.http.HttpServletRequest;

public interface LimitsService {
    Object getLimitsType(GetLimits request, HttpServletRequest servletRequest);

    Object getCardLimits(GetCardLimitRequest request, HttpServletRequest servletRequest);
    Object getCardLimitsBalance(GetCardLimitBalanceRequest request, HttpServletRequest servletRequest);

    Object setCardLimit(SetCardLimitRequest request, HttpServletRequest servletRequest);

    LimitListResponse getCardLimitsV2(GetCardLimitRequest request);

}
