package uz.company.leskarserver.leskarintegration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeskarGetFeeRequest(
        String originatorType,
        String sourceCurrency,
        String beneficiaryType,
        String targetCurrency,
        BigDecimal sourceAmount,
        String transferType,
        String targetCountry
) {
}
