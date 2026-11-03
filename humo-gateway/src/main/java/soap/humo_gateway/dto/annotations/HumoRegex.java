package soap.humo_gateway.dto.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = HumoRegexValidator.class)
public @interface HumoRegex {
    String message() default "{HumoRegex.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
