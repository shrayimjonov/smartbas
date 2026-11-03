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
public record LeskarGetFeeResponse(
        @JsonProperty("source_currency")
        String sourceCurrency,
        @JsonProperty("required_fields")
        RequiredFields requiredFields,
        @JsonProperty("fees")
        List<FeesItem> fees,
        @JsonProperty("target_amount")
        BigDecimal targetAmount,
        @JsonProperty("target_currency")
        String targetCurrency,
        @JsonProperty("rate")
        BigDecimal rate
) {
}