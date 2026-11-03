package uz.davrbank.yeisvoservice.aggregate.dto.response.report;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.davrbank.yeisvoservice.exception.controller.GlobalExceptionMessage;

@Getter
public enum TransType {

    PRITTOK(1),
    OTTOK(2);


    private final int code;

    TransType(int code) {
        this.code = code;
    }
    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static TransType fromCode(int code) {
        for (TransType t : values()) {
            if (t.code == code) {
                return t;
            }
        }
        throw new GlobalExceptionMessage(400, "Invalid transType value" , HttpStatus.BAD_REQUEST);
    }
}

