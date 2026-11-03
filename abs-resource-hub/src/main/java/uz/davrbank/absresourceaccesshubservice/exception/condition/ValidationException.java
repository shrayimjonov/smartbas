package uz.davrbank.absresourceaccesshubservice.exception.condition;

public class ValidationException extends RuntimeException {
    private final String field;
    private final String value;
    public ValidationException(String field, String value, String message) {
        super(message);
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

}
