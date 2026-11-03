package uz.davrbank.payment.utils;

import lombok.extern.slf4j.Slf4j;
import uz.davrbank.payment.model.dto.billing.request.CheckRequest;
import uz.davrbank.payment.model.dto.core.request.PayRequestDTO;

import java.util.UUID;

@Slf4j
public class RequestTools {
    public static CheckRequest checkRequestBuilder(PayRequestDTO payDTO, UUID internalId) {
        var checkRequest = CheckRequest.builder()
                .agentTranId(internalId)
                .amount(payDTO.getAmount())
                .recipientId(payDTO.getServiceId())
                .account(payDTO.getAccount())
                .build();
        log.info("Check request prepared: {}", checkRequest);
        return checkRequest;
    }



}
