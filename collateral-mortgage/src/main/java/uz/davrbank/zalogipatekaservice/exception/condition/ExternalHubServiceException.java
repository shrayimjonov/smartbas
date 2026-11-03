package uz.davrbank.zalogipatekaservice.exception.condition;

import org.springframework.web.client.RestClientException;

public class ExternalHubServiceException extends RuntimeException {
    public ExternalHubServiceException(String message, RestClientException ex) {
        super(message);
    }
}
