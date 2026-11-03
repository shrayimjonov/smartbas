package uz.company.leskarserver.leskarintegration;

import java.math.BigDecimal;
import java.util.List;

public record LeskarGetFeeResponse(
        String sourceCurrency,
        RequiredFields requiredFields,
        List<FeesItem> fees,
        BigDecimal targetAmount,
        String targetCurrency,
        BigDecimal rate
) {
}