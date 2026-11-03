package soap.humo_gateway.exception;

public class ConnectionError extends RuntimeException {
    public ConnectionError(String message) {
        super(message);
    }
}
