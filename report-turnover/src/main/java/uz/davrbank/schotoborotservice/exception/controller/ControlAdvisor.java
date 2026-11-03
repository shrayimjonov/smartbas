package uz.davrbank.schotoborotservice.exception.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import uz.davrbank.schotoborotservice.aggregate.dto.base.CodeError;
import uz.davrbank.schotoborotservice.aggregate.dto.base.ResultData;
import uz.davrbank.schotoborotservice.exception.condition.*;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 15.12.2025
 * Time: 13:29
 * Project: schotoborot-service
 */

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ControlAdvisor {


    ObjectMapper objectMapper;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResultData<CodeError>> on(IllegalArgumentException e) {
        var status = HttpStatus.BAD_REQUEST;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(CustomInternalServerException.class)
    public ResponseEntity<ResultData<CodeError>> on(CustomInternalServerException e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResultData<CodeError>> on(ResponseStatusException e) {
        var status = HttpStatus.resolve(e.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(StringUtils.hasText(e.getReason()) ? e.getReason() : e.getMessage());
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ResultData<CodeError>> on(DateTimeException e) {
        var status = HttpStatus.BAD_REQUEST;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(GeneralErrorException.class)
    public ResponseEntity<ResultData<CodeError>> on(GeneralErrorException e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(NotFormatSuitableException.class)
    public ResponseEntity<ResultData<CodeError>> on(NotFormatSuitableException e) {
        var status = HttpStatus.BAD_REQUEST;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResultData<CodeError>> on(BadRequestException e) {
        var status = HttpStatus.BAD_REQUEST;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResultData<CodeError>> on(NotFoundException e) {
        var status = HttpStatus.NOT_FOUND;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultData<CodeError>> on(Exception e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        logExceptionMessage(error);
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    private void logError(Exception e, HttpStatus status) {
        log.error("Error: status={} msg={}", status.value(), e.getMessage());
    }

    private void logExceptionMessage(CodeError error) {
        try {
            log.error("Error: {}", objectMapper.writeValueAsString(error));
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
