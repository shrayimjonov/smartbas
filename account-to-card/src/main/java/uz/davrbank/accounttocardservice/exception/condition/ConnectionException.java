package uz.davrbank.accounttocardservice.exception.condition;

import lombok.experimental.StandardException;

@StandardException
public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
}
