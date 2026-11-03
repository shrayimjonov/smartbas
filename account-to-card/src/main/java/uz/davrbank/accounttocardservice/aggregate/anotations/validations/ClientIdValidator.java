package uz.davrbank.accounttocardservice.aggregate.anotations.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.davrbank.accounttocardservice.aggregate.anotations.ClientId;

public class ClientIdValidator implements ConstraintValidator<ClientId, Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return o!=null && o.toString().matches("^\\d{8}$");
    }
}
