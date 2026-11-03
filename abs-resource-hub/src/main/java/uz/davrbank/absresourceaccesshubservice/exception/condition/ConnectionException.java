package uz.davrbank.absresourceaccesshubservice.exception.condition;

import lombok.experimental.StandardException;

@StandardException
public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
}
