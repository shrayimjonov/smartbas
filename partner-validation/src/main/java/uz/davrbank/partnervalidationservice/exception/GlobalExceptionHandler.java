package uz.davrbank.partnervalidationservice.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import uz.davrbank.partnervalidationservice.common.api.ResponseDTO;
import uz.davrbank.partnervalidationservice.constants.AuthConstants;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MerchantNotFoundException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleMerchantNotFound(MerchantNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MerchantAccountNotFoundException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleMerchantAccountNotFound(MerchantAccountNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleValidationFailed(ValidationFailedException exception) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
    }

    @ExceptionHandler(GatewayCommunicationException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleGatewayCommunication(GatewayCommunicationException exception) {
        HttpStatusCode statusCode = exception.getStatusCode() == null ? HttpStatus.BAD_GATEWAY : exception.getStatusCode();
        return buildResponse(statusCode, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleConstraintViolation(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid value for parameter '%s': %s".formatted(exception.getName(), exception.getValue())
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Request body is malformed or contains invalid values");
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleConversionFailed(ConversionFailedException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Request parameter conversion failed");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleIllegalArgument(IllegalArgumentException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        return buildResponse(HttpStatus.CONFLICT, "Request violates database constraints");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleGeneric(Exception exception) {
        log.error("Unhandled exception", exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    private ResponseEntity<ResponseDTO<ErrorResponse>> buildResponse(HttpStatusCode statusCode, String message) {
        String correlationId = resolveCorrelationId();
        HttpStatus httpStatus = HttpStatus.resolve(statusCode.value());
        ErrorResponse errorResponse = new ErrorResponse(
                OffsetDateTime.now(),
                statusCode.value(),
                httpStatus == null ? "Unknown Status" : httpStatus.getReasonPhrase(),
                message,
                correlationId == null ? "" : correlationId
        );
        return ResponseEntity.status(statusCode).body(ResponseDTO.error(errorResponse));
    }

    private String resolveCorrelationId() {
        String correlationId = MDC.get(AuthConstants.CORRELATION_ID_HEADER);
        if (correlationId != null && !correlationId.isBlank()) {
            return correlationId;
        }
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        return attributes.getRequest().getHeader(AuthConstants.CORRELATION_ID_HEADER);
    }
}
