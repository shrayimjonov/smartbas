package uz.davrbank.enums;

import lombok.Getter;

@Getter
public enum LanguageType {
    uz("uz"),
    ru("ru"),
    en("en");

    private final String code;

    LanguageType(String code) {
        this.code = code;
    }

}
