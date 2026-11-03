package uz.davrbank.educontrolservice.exception.condition;

import java.io.Serial;

public class ConnectionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4470414516966466003L;

    public ConnectionException(String message) {
        super(message);
    }
}
