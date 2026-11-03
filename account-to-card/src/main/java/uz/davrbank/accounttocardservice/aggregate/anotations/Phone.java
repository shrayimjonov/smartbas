package uz.davrbank.accounttocardservice.aggregate.anotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uz.davrbank.accounttocardservice.aggregate.anotations.validations.PhoneValidator;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String message() default "{Phone.message}";

    String example() default "{Phone.example}";

    String regex() default "^\\+?\\d{9,15}$";


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}