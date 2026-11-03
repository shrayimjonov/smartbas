package soap.humo_gateway.exception;

public class GatewayError extends RuntimeException{
    public GatewayError(String message) {
        super(message);
    }
}
