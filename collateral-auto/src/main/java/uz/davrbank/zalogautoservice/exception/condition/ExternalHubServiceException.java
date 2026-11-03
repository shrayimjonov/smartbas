package uz.davrbank.zalogautoservice.exception.condition;

public class ExternalHubServiceException extends RuntimeException {
    public ExternalHubServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}