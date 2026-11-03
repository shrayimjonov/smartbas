package uz.davrbank.partnervalidationservice.validation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ValidationRequest(
        @NotBlank String merchantId,
        String requestId,
        @NotNull LocalDateTime timestamp,
        @Valid @NotNull Payload payload
) {

    public record Payload(
            @JsonProperty("P_ACC_CL")
            @NotBlank
            String payerAccount,

            @JsonProperty("P_ACC_CO")
            String beneficiaryAccount,

            @JsonProperty("P_NAME_CO")
            String beneficiaryName,

            @JsonProperty("P_BANK_CO")
            String beneficiaryBankCode,

            @JsonProperty("P_INN_CO")
            String beneficiaryTin,

            @JsonProperty("P_SUMMA")
            @DecimalMin("0.0")
            BigDecimal amount,

            @JsonProperty("P_DOC_NUM")
            String documentNumber,

            @JsonProperty("P_DATE")
            @JsonFormat(pattern = "dd.MM.yyyy")
            LocalDate documentDate,

            @JsonProperty("P_PURPOSE_ID")
            String purposeId,

            @JsonProperty("P_PURPOSE")
            String purpose,

            @JsonProperty("P_TYPE_DOC")
            String documentType,

            @JsonProperty("P_TYPE_PLAT")
            Integer paymentType,

            @JsonProperty("P_STATE")
            @NotNull
            Integer state,

            @JsonProperty("P_BRANCH")
            @NotBlank
            String branch
    ) {
    }
}
