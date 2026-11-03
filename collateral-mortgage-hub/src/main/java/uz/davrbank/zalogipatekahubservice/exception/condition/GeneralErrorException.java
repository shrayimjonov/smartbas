package uz.davrbank.zalogipatekahubservice.exception.condition;

import lombok.experimental.StandardException;

@StandardException
public class GeneralErrorException extends RuntimeException {
    public GeneralErrorException(String message) {
        super(message);
    }
}
