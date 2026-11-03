package uz.davrbank.config.annotations.fileValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileTypeValidator.class)
@Documented
public @interface FileTypeValid {

    String message() default "Unsupported file type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

   FileType[] allowedTypes() default {};
}
