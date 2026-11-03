package uz.davrbank.partnervalidationservice.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.partnervalidationservice.logging.enums.MonitoringStep;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValidationFailedException extends RuntimeException {

    MonitoringStep step;

    public ValidationFailedException(MonitoringStep step, String message) {
        super(message);
        this.step = step;
    }
}
