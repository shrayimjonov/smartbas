package uz.davrbank.educontrolservice.exception.condition;

public class DatabaseEmptyException extends RuntimeException {
    public DatabaseEmptyException(String message) {
        super(message);
    }
}