package uz.davrbank.journaltransservice.exception.condition;

public class CustomInternalServerException extends RuntimeException {
    public CustomInternalServerException(String message) {
        super(message);
    }
}
