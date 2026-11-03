package uz.devops.humoMiddleware.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Lang {
    @JsonProperty(value = "ru_translit")
    RU,
    @JsonProperty(value = "en")
    EN;
}

