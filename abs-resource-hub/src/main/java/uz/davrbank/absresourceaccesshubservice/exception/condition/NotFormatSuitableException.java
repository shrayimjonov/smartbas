package uz.davrbank.absresourceaccesshubservice.exception.condition;

import lombok.experimental.StandardException;
@StandardException
public class NotFormatSuitableException extends RuntimeException {
    public NotFormatSuitableException(String message) {
        super(message);
    }
}
