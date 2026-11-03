package uz.davrbank.config.annotations.fileValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileTypeValidator implements ConstraintValidator<FileTypeValid, MultipartFile> {

    private FileType[] allowedTypes;

    @Override
    public void initialize(FileTypeValid constraintAnnotation) {
        this.allowedTypes = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        try {
            return FileValidator.validateFile(file, allowedTypes);
        } catch (IOException e) {
            return false;
        }
    }
}
