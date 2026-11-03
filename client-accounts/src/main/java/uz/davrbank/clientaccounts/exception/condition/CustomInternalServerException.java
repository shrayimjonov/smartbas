package uz.davrbank.clientaccounts.exception.condition;

public class CustomInternalServerException extends RuntimeException {
    public CustomInternalServerException(String message) {
        super(message);
    }
}
