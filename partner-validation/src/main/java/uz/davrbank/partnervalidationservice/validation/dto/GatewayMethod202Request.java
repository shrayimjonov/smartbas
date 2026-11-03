package uz.davrbank.partnervalidationservice.validation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GatewayMethod202Request(
        @JsonProperty("P_ACC_ID")
        String accountId,
        @JsonProperty("P_NAME")
        String name,
        @JsonProperty("P_CLIENT")
        String client,
        @JsonProperty("P_ACC_BAL")
        String accountBalance,
        @JsonProperty("P_CURRENCY")
        String currency,
        @JsonProperty("P_ID_ORDER")
        String orderId,
        @JsonProperty("P_STATE")
        Integer state,
        @JsonProperty("P_BRANCH")
        String branch
) {
}
