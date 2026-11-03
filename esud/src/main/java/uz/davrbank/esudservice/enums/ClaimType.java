package uz.davrbank.esudservice.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum ClaimType {
    CONFLICT,
    CIVIL,
    CRIMINAL,
    ADMINISTRATIVE,
    ECONOMIC;

    public static ClaimType findClaimTypeByName(final String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }

        return Arrays.stream(values())
                .filter(lang -> lang.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
