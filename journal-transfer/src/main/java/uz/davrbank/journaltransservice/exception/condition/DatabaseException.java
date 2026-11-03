package uz.davrbank.journaltransservice.exception.condition;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
