package uz.davrbank.partnervalidationservice.validation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GatewayMethod105Request(
        @JsonProperty("P_NUMBER_TAX_REGISTRATION")
        String numberTaxRegistration,
        @JsonProperty("P_BRANCH")
        String branch
) {
}
