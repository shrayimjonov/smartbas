package davr.uzcard.Exception;

import lombok.experimental.StandardException;

@StandardException
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "NotFoundException: " + getMessage();
    }
}
