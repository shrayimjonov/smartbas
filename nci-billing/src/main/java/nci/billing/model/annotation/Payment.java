package nci.billing.model.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PaymentValidator.class)
public @interface Payment {
    String message() default "{Payment.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
