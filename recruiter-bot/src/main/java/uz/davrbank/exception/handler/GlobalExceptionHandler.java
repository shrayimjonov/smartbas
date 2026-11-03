package uz.davrbank.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.enums.ResponseStatus;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.exception.condition.BadRequestException;
import uz.davrbank.exception.condition.CustomInternalServerException;
import uz.davrbank.exception.condition.DateTimeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TelegramApiException.class)
    public ResponseEntity<ResultData<ResponseDto>> handleTelegramApiException(TelegramApiException ex) {
        log.error("TelegramApiException: {}", ex.getMessage());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.BAD_REQUEST, "Telegram API error: " + ex.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResultData<ResponseDto>> IOException(IOException ex) {
        log.error("IO Error occurred: {}", ex.getMessage());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.BAD_REQUEST, "IO Error: " + ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResultData<ResponseDto>> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        log.error("MissingServletRequestParameterException: {}", name);
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.BAD_REQUEST, "Missing required parameter: " + name));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultData<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.BAD_REQUEST, "Validation failed  " + errors));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResultData<ResponseDto>> handleDepartmentNotFoundException(NotFoundException ex) {
        log.error("NotFoundException: {}", ex.getMessage());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ResultData<String>> handleMissingPathVariable(MissingPathVariableException ex) {
        log.error("MissingPathVariableException: {}", ex.getMessage());
        String message = String.format("Path variable '%s' is required", ex.getVariableName());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResultData<ResponseDto>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.BAD_REQUEST, "Data integrity violation: " + ex.getMostSpecificCause().getMessage()));
    }

    @ExceptionHandler(CustomInternalServerException.class)
    public ResponseEntity<ResultData<ResponseDto>> handleDataIntegrityViolationException(CustomInternalServerException ex) {
        log.error("CustomInternalServerException: {}", ex.getMessage());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.FAILED, "Data integrity violation: " + ex.getMessage()));
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<ResultData<ResponseDto>> handleDataIntegrityViolationException(HttpServerErrorException.InternalServerError ex) {
        log.error("InternalServerError: {}", ex.getMessage());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.FAILED, "Internal server error" + ex.getMessage()));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ResultData<ResponseDto>> handleDataIntegrityViolationException(DateTimeException ex) {
        log.error("DatetimeFormatException: {}", ex.getMessage());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.FAILED, "Date time format error: " + ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResultData<ResponseDto>> handleDataIntegrityViolationException(BadRequestException ex) {
        log.error("BadRequestException: {}", ex.getMessage());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.FAILED, "BadRequestException: " + ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResultData<ResponseDto>> handleDataIntegrityViolationException(RuntimeException ex) {
        log.error("RuntimeException: {}", ex.getMessage());
        return ResponseEntity.ok(new ResultData<>(false, ResponseStatus.FAILED, "Internal server error: " + ex.getMessage()));
    }

}
