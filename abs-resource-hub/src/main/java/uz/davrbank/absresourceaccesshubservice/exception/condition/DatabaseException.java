package uz.davrbank.absresourceaccesshubservice.exception.condition;

import lombok.experimental.StandardException;
@StandardException
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
