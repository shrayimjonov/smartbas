package uz.davrbank.accounttocardservice.aggregate.anotations.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uz.davrbank.accounttocardservice.aggregate.anotations.Expiry;
import uz.davrbank.accounttocardservice.aggregate.property.CardProperty;

@Slf4j
@RequiredArgsConstructor
public class ExpiryValidator implements ConstraintValidator<Expiry, String> {
    private final CardProperty cardProperty;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}