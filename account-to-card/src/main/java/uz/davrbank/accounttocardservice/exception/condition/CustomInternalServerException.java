package uz.davrbank.accounttocardservice.exception.condition;

import lombok.experimental.StandardException;

@StandardException
public class CustomInternalServerException extends RuntimeException {
    public CustomInternalServerException(String message) {
        super(message);
    }
}
