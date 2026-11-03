package uz.davrbank.accounttocardservice.aggregate.anotations.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.davrbank.accounttocardservice.aggregate.anotations.Pan;


public class PanValidator implements ConstraintValidator<Pan, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return o != null && o.toString().matches("^\\d{16}$");
    }
}
