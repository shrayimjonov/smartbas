package uz.davrbank.esudservice.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum Citizenship {
    UZB_CITIZEN,
    FOREIGNER,
    NOT_CITIZEN;

    public static Citizenship findCitizenshipByName(final String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }

        return Arrays.stream(values())
                .filter(lang -> lang.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
