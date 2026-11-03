package nci.billing.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.annotation.System;
import nci.billing.model.enums.SystemType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayDtoV2 {
    @NotBlank
    @Pattern(regexp = "^\\d{9,}$")
    @JsonProperty("loanId")
    private String loanId;
    @NotBlank
    @JsonProperty("terminalId")
    private String terminalId;
    @NotBlank
    @JsonProperty("merchantId")
    private String merchantId;
    @NotBlank
    @Pattern(regexp = "^[0-9]*$")
    @JsonProperty("paymentId")
    private String paymentId;
    @NotBlank
    @JsonProperty("refNum")
    private String refNum;
    @Min(1)
    @JsonProperty("amount")
    private Long amount = 0L;
    @JsonProperty("systemType")
    @System
    private SystemType systemType;
    @JsonProperty("cardNum")
    private String cardNum;
    @NotBlank
    @Pattern(regexp = "\\d{20}")
    @JsonProperty("account")
    private String account;


}
