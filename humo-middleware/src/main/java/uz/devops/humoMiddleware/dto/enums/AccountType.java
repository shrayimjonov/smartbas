package uz.devops.humoMiddleware.dto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AccountType {
    @JsonProperty("DEFAULT")
    DEFAULT,
    @JsonProperty("Offline chip")
    OFFLINE_CHIP,
    @JsonProperty("Loyalty")
    LOYALTY,
    @JsonProperty("Savings")
    SAVINGS,
    @JsonProperty("Cheque")
    CHEQUE,
    @JsonProperty("Credit")
    CREDIT,
    @JsonProperty("Universal")
    UNIVERSAL,
    @JsonProperty("Investment")
    INVESTMENT
}
