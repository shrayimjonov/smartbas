package uz.davrbank.bustreasury.exeption.controller;

import java.io.Serializable;

public record ErrorResponse(int code, String message, Object metaData) implements Serializable {
    // Constructor

    public static ErrorResponse of(ResponseCode responseCode, Object... params) {
        return new ErrorResponse(responseCode.getCode(), responseCode.getMessage(), params);
    }
}