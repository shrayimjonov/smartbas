package uz.davrbank.bustreasury.exeption;

import java.io.Serial;


public class DataNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7034897190745766939L;

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}