package soap.humo_gateway.exception;

public class ErrorWithDesc extends RuntimeException{
    public ErrorWithDesc(String message) {
        super(message);
    }
}
