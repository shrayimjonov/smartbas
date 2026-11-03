package uz.davrbank.accounttocardservice.exception.condition;

import lombok.experimental.StandardException;

@StandardException
public class DateTimeException extends RuntimeException {
    public DateTimeException(String message) {
        super(message);
    }
}
