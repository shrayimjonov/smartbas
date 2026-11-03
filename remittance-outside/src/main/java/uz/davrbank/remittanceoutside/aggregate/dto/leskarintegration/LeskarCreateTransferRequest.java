/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LeskarCreateTransferRequest(
        @JsonProperty("source_doc_number")
        String sourceDocNumber,
        @JsonProperty("target_account_number")
        String targetAccountNumber,
        @JsonProperty("source_account_number")
        String sourceAccountNumber,
        @JsonProperty("originator_id")
        String originatorId,
        @JsonProperty("beneficiary_id")
        String beneficiaryId,
        @JsonProperty("purpose")
        String purpose,
        @JsonProperty("sof")
        String sof,
        @JsonProperty("transfer_type")
        String transferType,
        @JsonProperty("target_country")
        String targetCountry,
        @JsonProperty("target_currency")
        String targetCurrency,
        @JsonProperty("source_currency")
        String sourceCurrency,
        @JsonProperty("source_amount")
        BigDecimal sourceAmount,
        @JsonProperty("additional")
        List<AdditionalItem> additional
) {
}
