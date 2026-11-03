package soap.humo_gateway.exception;

import lombok.experimental.StandardException;

@StandardException
public class HumoErrorResponse extends RuntimeException{
    public HumoErrorResponse(String message) {
        super(message);
    }
}
