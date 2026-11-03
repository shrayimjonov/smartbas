package uz.davrbank.usermanualservice.exception.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.davrbank.usermanualservice.aggregate.model.CodeError;
import uz.davrbank.usermanualservice.aggregate.model.ResultData;
import uz.davrbank.usermanualservice.exception.condition.CustomInternalServerException;
import uz.davrbank.usermanualservice.exception.condition.DateTimeException;
import uz.davrbank.usermanualservice.exception.condition.GeneralErrorException;
import uz.davrbank.usermanualservice.exception.condition.NotFormatSuitableException;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 25.11.2025
 * Time: 11:52
 * Project: usermanual-service
 */

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControlAdvisor {


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResultData<CodeError>> on(IllegalArgumentException e) {
        var status = HttpStatus.BAD_REQUEST;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(CustomInternalServerException.class)
    public ResponseEntity<ResultData<CodeError>> on(CustomInternalServerException e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ResultData<CodeError>> on(DateTimeException e) {
        var status = HttpStatus.BAD_REQUEST;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(GeneralErrorException.class)
    public ResponseEntity<ResultData<CodeError>> on(GeneralErrorException e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(NotFormatSuitableException.class)
    public ResponseEntity<ResultData<CodeError>> on(NotFormatSuitableException e) {
        var status = HttpStatus.BAD_REQUEST;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultData<CodeError>> on(Exception e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    private void logError(Exception e, HttpStatus status) {
        log.error("Error: status={} msg={}", status.value(), e.getMessage());
    }
}