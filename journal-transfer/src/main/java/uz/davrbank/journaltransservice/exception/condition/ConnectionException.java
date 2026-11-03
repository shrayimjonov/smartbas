package uz.davrbank.journaltransservice.exception.condition;

public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
}
