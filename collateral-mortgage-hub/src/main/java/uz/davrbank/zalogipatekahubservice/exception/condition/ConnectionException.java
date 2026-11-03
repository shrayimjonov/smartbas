package uz.davrbank.zalogipatekahubservice.exception.condition;

import lombok.experimental.StandardException;

import java.io.Serial;
@StandardException
public class ConnectionException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -4470414516966466003L;
}
