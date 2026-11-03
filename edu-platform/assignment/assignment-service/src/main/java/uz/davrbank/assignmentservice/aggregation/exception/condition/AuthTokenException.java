package uz.davrbank.assignmentservice.aggregation.exception.condition;

public class AuthTokenException extends RuntimeException {
    public AuthTokenException(String message) {
        super(message);
    }

    public AuthTokenException(Throwable cause) {
        super(cause);
    }
}
