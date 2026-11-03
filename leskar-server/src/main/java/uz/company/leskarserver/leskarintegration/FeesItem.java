package uz.company.leskarserver.leskarintegration;

import java.math.BigDecimal;

public record FeesItem(
        BigDecimal amount,
        String currency,
        String type,
        BigDecimal percent
) {
}
