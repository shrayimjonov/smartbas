package uz.davrbank.esudservice.aggregate.feign;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * A Feign {@link Decoder} implementation that validates deserialized response objects
 * using Bean Validation ({@link Validator}) annotations.
 * <p>
 * This decoder delegates the actual deserialization to another {@link Decoder} and
 * then performs validation on the resulting object. If any validation constraints
 * are violated, a {@link ConstraintViolationException} is thrown.
 * </p>
 *
 * <p>
 * Typical usage: wrap the standard Spring decoder to automatically validate
 * response DTOs received from external services.
 * </p>
 *
 * <pre>{@code
 * Decoder decoder = new ValidatingDecoder(new SpringDecoder(...), validator);
 * }</pre>
 */
public record ValidatingDecoder(Decoder delegate, Validator validator) implements Decoder {

    /**
     * Decodes the given Feign {@link Response} into an object of the specified {@link Type},
     * then validates it using Bean Validation.
     *
     * @param response the Feign {@link Response} to decode
     * @param type the target {@link Type} to deserialize the response into
     * @return the deserialized and validated object
     * @throws IOException if an I/O error occurs during decoding
     * @throws FeignException if a Feign-specific error occurs
     * @throws ConstraintViolationException if the deserialized object violates any Bean Validation constraints
     */
    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {

        // 1) Deserialize the response using the delegate
        Object obj = delegate.decode(response, type);

        // 2) Perform validation
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);

        if (!violations.isEmpty()) {
            String msg = violations.iterator().next().getMessage();
            throw new ConstraintViolationException(msg, violations);
        }

        return obj;
    }
}
