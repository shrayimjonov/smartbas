package uz.davrbank.zalogautoservice.exception.condition;

public class CustomInternalServerException extends RuntimeException {
    public CustomInternalServerException(String message) {
        super(message);
    }
}
