package uz.davrbank.educontrolservice.exception.condition;

public class CustomInternalServerException extends RuntimeException {
    public CustomInternalServerException(String message) {
        super(message);
    }
}
