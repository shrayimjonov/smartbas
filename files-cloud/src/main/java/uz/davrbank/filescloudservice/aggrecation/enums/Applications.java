package uz.davrbank.filescloudservice.aggrecation.enums;

import lombok.Getter;


@Getter
public enum Applications {
    ZALOG_AUTO("reports/zalog/auto"),
    ZALOG_IPATEKA("reports/zalog/ipoteka"),
    YEISVO("reports/yeisvo");
    private final String path;

    Applications(String path) {
        this.path = path;
    }
}