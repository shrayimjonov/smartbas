package uz.davrbank.assignmentservice.aggregation.exception.condition;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;

@Getter
@Setter
public class HttpStatusException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private HttpStatusCode statusCode;
    private String message;

    public HttpStatusException(HttpStatusCode statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.message = message;
    }

}