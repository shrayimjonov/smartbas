package uz.davrbank.accounttocardservice.aggregate.anotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uz.davrbank.accounttocardservice.aggregate.anotations.validations.ClientIdValidator;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ClientIdValidator.class})
public @interface ClientId {

    String message() default "{ClientId.message}";
    String example() default "{ClientId.example}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
