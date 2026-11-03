package uz.davrbank.educontrolservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * Validator for date range validation.
 * Ensures end date is after start date.
 * Supports LocalDate and LocalDateTime.
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            BeanWrapper beanWrapper = new BeanWrapperImpl(value);
            Object startValue = beanWrapper.getPropertyValue(startDateField);
            Object endValue = beanWrapper.getPropertyValue(endDateField);

            if (startValue == null || endValue == null) {
                return true; // Let @NotNull handle null validation
            }

            if (startValue instanceof LocalDateTime start && endValue instanceof LocalDateTime end) {
                return end.isAfter(start);
            }

            if (startValue instanceof LocalDate start && endValue instanceof LocalDate end) {
                return end.isAfter(start);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

