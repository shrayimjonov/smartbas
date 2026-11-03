package uz.davrbank.accounttocardservice.aggregate.anotations.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.davrbank.accounttocardservice.aggregate.anotations.Phone;

import java.util.regex.Pattern;


public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private Pattern phonePattern;

    @Override
    public void initialize(Phone constraintAnnotation) {
        this.phonePattern = Pattern.compile(constraintAnnotation.regex());
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }
        return phonePattern.matcher(phoneNumber).matches();
    }
}
