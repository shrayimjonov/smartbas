package uz.davrbank.zalogautoservice.exception.condition;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
