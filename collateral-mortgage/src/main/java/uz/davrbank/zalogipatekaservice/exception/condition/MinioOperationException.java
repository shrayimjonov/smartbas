package uz.davrbank.zalogipatekaservice.exception.condition;

public class MinioOperationException extends RuntimeException {
    public MinioOperationException(String message, Exception e) {
        super(message);
    }
}
