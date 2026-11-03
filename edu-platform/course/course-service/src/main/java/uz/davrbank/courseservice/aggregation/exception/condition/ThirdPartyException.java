package uz.davrbank.courseservice.aggregation.exception.condition;

import java.io.Serial;

public class ThirdPartyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4470414516966466003L;

    public ThirdPartyException(String message) {
        super(message);
    }

    public ThirdPartyException(String message, Throwable cause) {
        super(message, cause);
    }
}