package uz.davrbank.esudservice.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum PayableType {
    DEPT,
    MORAL_DAMAGE,
    MATERIAL_DAMAGE,
    LOST_PROFIT,
    PREPAID_EXPENSE,
    PENALTY,
    FINE,
    PERCENT,
    POST,
    VCC,
    STATE;

    public static PayableType findPayableTypeByName(final String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }

        return Arrays.stream(values())
                .filter(lang -> lang.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
