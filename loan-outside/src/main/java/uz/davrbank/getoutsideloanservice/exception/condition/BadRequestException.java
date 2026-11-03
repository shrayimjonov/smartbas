package uz.davrbank.getoutsideloanservice.exception.condition;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
