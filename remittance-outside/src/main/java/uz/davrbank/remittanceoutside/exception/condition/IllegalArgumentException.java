package uz.davrbank.remittanceoutside.exception.condition;

import lombok.experimental.StandardException;

@StandardException
public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(String message) {
        super(message);
    }
}
