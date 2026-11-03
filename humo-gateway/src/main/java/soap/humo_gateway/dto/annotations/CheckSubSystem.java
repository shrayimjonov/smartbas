package soap.humo_gateway.dto.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CheckSubSystemValidator.class)
public @interface CheckSubSystem {
    String message() default "{CheckSubSystem.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
