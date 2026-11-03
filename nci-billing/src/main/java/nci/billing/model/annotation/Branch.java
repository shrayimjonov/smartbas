package nci.billing.model.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BranchValidator.class)
public @interface Branch {
    String message() default "{Branch.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
