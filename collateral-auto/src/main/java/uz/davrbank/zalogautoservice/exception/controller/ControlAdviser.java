package uz.davrbank.zalogautoservice.exception.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import uz.davrbank.zalogautoservice.exception.condition.*;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControlAdviser {

    private static final Logger LOG = LoggerFactory.getLogger(ControlAdviser.class);

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponse> on(AccessDeniedException e) {
        LOG.error("Access Denied: {}", e.getMessage());
        ErrorResponse access = ErrorResponse.of(ResponseCode.ACCESS_DENIED, e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(access);
    }

    @ExceptionHandler(NotFormatSuitableException.class)
    ResponseEntity<ErrorResponse> on(NotFormatSuitableException e) {
        LOG.error("Not Format Suitable Exception: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.NOT_FORMAT_SUITABLE_EXCEPTION, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConnectionException.class)
    ResponseEntity<ErrorResponse> on(ConnectionException e) {
        LOG.error("Connection Exception: {}", e.getMessage());
        ErrorResponse error = ErrorResponse.of(ResponseCode.CONNECTION_EXCEPTION, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(CustomInternalServerException.class)
    ResponseEntity<ErrorResponse> on(CustomInternalServerException exception) {
        LOG.error("Custom internal server exception: {}", exception.getMessage());
        ErrorResponse exceptionResult = ErrorResponse.of(ResponseCode.CONNECTION_EXCEPTION, exception.getMessage());
        return new ResponseEntity<>(exceptionResult, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> on(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().isEmpty()
                ? "Validation failed"
                : ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        LOG.error("Client sent the wrong request: {}", errorMessage);
        ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.REQUIRED_DATA_MISSING, errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DatabaseException.class)
    ResponseEntity<ErrorResponse> on(DatabaseException ex) {
        LOG.error("Database Exception: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.DATABASE, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthTokenException.class)
    ResponseEntity<ErrorResponse> on(AuthTokenException ex) {
        LOG.error("AuthToken Exception: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.TOKEN, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<?> on(ResponseStatusException ex) {
        String message = ex.getReason() != null ? ex.getReason() : ex.getMessage();
        return new ResponseEntity<>(ErrorResponse.of(ex.getStatusCode().value(), message), ex.getStatusCode());
    }
}
