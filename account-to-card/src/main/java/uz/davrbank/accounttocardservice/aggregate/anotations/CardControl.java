package uz.davrbank.accounttocardservice.aggregate.anotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uz.davrbank.accounttocardservice.aggregate.anotations.validations.ExpiryValidator;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ExpiryValidator.class)
public @interface CardControl {
    String message() default "{cardControl.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}