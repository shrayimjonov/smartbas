package uz.davrbank.esudservice.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum ClaimKind {
    SUIT,
    MATERIAL,
    SANCTION,
    DECREE,
    ARBITRAGE,
    SEPARATE,
    SIMPLIFIED;

    public static ClaimKind findClaimKindByName(final String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }

        return Arrays.stream(values())
                .filter(lang -> lang.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
