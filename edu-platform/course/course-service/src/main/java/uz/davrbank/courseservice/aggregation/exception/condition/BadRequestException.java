package uz.davrbank.courseservice.aggregation.exception.condition;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
