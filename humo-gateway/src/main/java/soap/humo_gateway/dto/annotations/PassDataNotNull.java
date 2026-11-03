package soap.humo_gateway.dto.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassDataValidator.class)
@Documented
public @interface PassDataNotNull {


    String message() default "{PassDataNotNull.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
