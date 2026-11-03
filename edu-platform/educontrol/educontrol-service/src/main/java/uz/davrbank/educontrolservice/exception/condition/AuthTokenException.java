package uz.davrbank.educontrolservice.exception.condition;

public class AuthTokenException extends RuntimeException {
    public AuthTokenException(Throwable cause) {
        super(cause);
    }

    public AuthTokenException(String message) {
        super(message);
    }
}
