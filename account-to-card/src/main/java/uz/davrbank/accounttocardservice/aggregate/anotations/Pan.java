package uz.davrbank.accounttocardservice.aggregate.anotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uz.davrbank.accounttocardservice.aggregate.anotations.validations.PanValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PanValidator.class)
public @interface Pan {

    String message() default "Invalid pan number";
    String example() default "{Pan example}";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}