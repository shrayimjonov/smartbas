package uz.davrbank.partnervalidationservice.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GatewayCommunicationException extends RuntimeException {

    HttpStatusCode statusCode;

    public GatewayCommunicationException(String message) {
        this(message, null, null);
    }

    public GatewayCommunicationException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public GatewayCommunicationException(String message, HttpStatusCode statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
