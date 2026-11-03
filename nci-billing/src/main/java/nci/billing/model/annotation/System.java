package nci.billing.model.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SystemValidator.class)
public @interface System {
    String message() default "{System.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
