package nci.billing.model.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.model.enums.SystemType;

import java.util.Arrays;


@Slf4j
@RequiredArgsConstructor
public class SystemValidator implements ConstraintValidator<System, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        var result = o != null && Arrays.asList(SystemType.values()).contains(((SystemType) o));
        log.info("system annotation validator result: {}", result);
        return result;
    }
}