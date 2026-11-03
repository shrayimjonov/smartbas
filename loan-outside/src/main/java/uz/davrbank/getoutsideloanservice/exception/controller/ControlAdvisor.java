package uz.davrbank.getoutsideloanservice.exception.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import uz.davrbank.getoutsideloanservice.aggregate.dto.base.CodeError;
import uz.davrbank.getoutsideloanservice.aggregate.dto.base.ResultData;
import uz.davrbank.getoutsideloanservice.exception.condition.BadRequestException;
import uz.davrbank.getoutsideloanservice.exception.condition.CustomInternalServerException;
import uz.davrbank.getoutsideloanservice.exception.condition.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ControlAdvisor {

    ObjectMapper objectMapper;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResultData<CodeError>> on(BadRequestException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResultData<CodeError>> on(NotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CustomInternalServerException.class)
    public ResponseEntity<ResultData<CodeError>> on(CustomInternalServerException e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultData<CodeError>> on(HttpMessageNotReadableException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed request body");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResultData<CodeError>> on(ResponseStatusException e) {
        return buildErrorResponse(HttpStatus.valueOf(e.getStatusCode().value()), e.getReason());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultData<CodeError>> on(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<ResultData<CodeError>> buildErrorResponse(HttpStatus status, String message) {
        log.error("Error: status={} msg={}", status.value(), message);
        CodeError error = new CodeError(status.value(), message);
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    private void logExceptionMessage(CodeError error) {
        try {
            log.error("Error body: {}", objectMapper.writeValueAsString(error));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize error body: {}", e.getMessage());
        }
    }
}
