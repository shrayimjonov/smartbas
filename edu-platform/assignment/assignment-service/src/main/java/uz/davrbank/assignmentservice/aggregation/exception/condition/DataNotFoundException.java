package uz.davrbank.assignmentservice.aggregation.exception.condition;


public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotFoundException(String message, String message1) {

    }
}
