package nci.billing.model.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.model.enums.PaymentType;

import java.util.Arrays;


@Slf4j
@RequiredArgsConstructor
public class PaymentValidator implements ConstraintValidator<Payment, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        var result = o != null && Arrays.asList(PaymentType.values()).contains((PaymentType) o);
        log.info("payment annotation validator result: {}", result);
        return result;
    }
}