package uz.davrbank.zalogipatekahubservice.exception.controller;

import lombok.Value;

import java.io.Serializable;
@Value(staticConstructor = "of")
public class ErrorResponse implements Serializable {

    int code;
    String message;
    Object metaData;

    public static ErrorResponse of(ResponseCode responseCode) {
        return of(responseCode.getCode(), responseCode.getMessage(), null);
    }

    public static ErrorResponse of(ResponseCode responseCode, Object... params) {
        return of(responseCode.getCode(), responseCode.getMessage(), params);
    }
}