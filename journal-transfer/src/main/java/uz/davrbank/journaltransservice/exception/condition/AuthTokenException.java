package uz.davrbank.journaltransservice.exception.condition;

public class AuthTokenException extends RuntimeException {
    public AuthTokenException(String message) {
        super(message);
    }
}
