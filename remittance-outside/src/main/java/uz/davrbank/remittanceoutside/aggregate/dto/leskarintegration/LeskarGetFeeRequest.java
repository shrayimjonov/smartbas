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

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LeskarGetFeeRequest(
        @JsonProperty("originator_type")
        String originatorType,
        @JsonProperty("source_currency")
        String sourceCurrency,
        @JsonProperty("beneficiary_type")
        String beneficiaryType,
        @JsonProperty("target_currency")
        String targetCurrency,
        @JsonProperty("source_amount")
        BigDecimal sourceAmount,
        @JsonProperty("transfer_type")
        String transferType,
        @JsonProperty("target_country")
        String targetCountry
) {
}
