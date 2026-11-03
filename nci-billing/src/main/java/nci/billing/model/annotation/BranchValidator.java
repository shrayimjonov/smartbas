package nci.billing.model.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
public class BranchValidator implements ConstraintValidator<Branch, Object> {

    @Value("${bank.branches}")
    private String branches;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        var result = o != null && o.toString().matches(branches);
        log.info("branch annotation validator result: {}", result);
        return result;
    }
}