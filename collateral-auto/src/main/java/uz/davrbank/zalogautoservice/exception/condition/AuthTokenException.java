package uz.davrbank.zalogautoservice.exception.condition;

public class AuthTokenException extends RuntimeException {
    public AuthTokenException(String message) {
        super(message);
    }
}
