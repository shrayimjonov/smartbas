package uz.davrbank.courseservice.aggregation.exception.condition;

import java.io.Serial;

public class ConnectionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4470414516966466003L;


    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
