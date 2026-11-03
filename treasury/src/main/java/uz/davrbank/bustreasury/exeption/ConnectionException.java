package uz.davrbank.bustreasury.exeption;

import java.io.Serial;


public class ConnectionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4470414516966466003L;

    public ConnectionException(Throwable cause) {
        super(cause);
    }

    public ConnectionException(String message) {
        super(message);
    }
}