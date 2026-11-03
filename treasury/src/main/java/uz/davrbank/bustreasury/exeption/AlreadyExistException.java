package uz.davrbank.bustreasury.exeption;

import java.io.Serial;


public class AlreadyExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1392000792596170370L;

    public AlreadyExistException(String message) {
        super(message);
    }

    public AlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}