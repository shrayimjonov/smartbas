package uz.davrbank.bustreasury.exeption;

import java.io.Serial;

public class AuthTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7034897190745766939L;

    public AuthTokenException(Throwable cause) {
        super(cause);
    }

    public AuthTokenException(String message) {
        super(message);
    }

}
