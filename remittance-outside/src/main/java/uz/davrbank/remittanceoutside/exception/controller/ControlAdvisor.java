/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.exception.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uz.davrbank.remittanceoutside.aggregate.model.CodeError;
import uz.davrbank.remittanceoutside.aggregate.model.ResultData;
import uz.davrbank.remittanceoutside.exception.condition.*;

import java.lang.IllegalArgumentException;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControlAdvisor {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ResultData<CodeError>> on(WebClientResponseException e) {
        var statusCode = e.getStatusCode();
        log.error("Error: status={} uri={} msg={}", statusCode.value(), e.getRequest() != null ? e.getRequest().getURI() : "n/a", e.getMessage());
        CodeError error = new CodeError();
        error.setCode(statusCode.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(statusCode).body(ResultData.error(error, false));
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ResultData<CodeError>> on(WebClientRequestException e) {
        var status = HttpStatus.BAD_GATEWAY;
        log.error("Error: status={} uri={} msg={}", status.value(), e.getUri(), e.getMessage());
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ResultData<CodeError>> on(TimeoutException e) {
        var status = HttpStatus.GATEWAY_TIMEOUT;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResultData<CodeError>> on(IllegalArgumentException e) {
        var status = HttpStatus.BAD_REQUEST;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<ResultData<CodeError>> on(IntegrationException e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }


    @ExceptionHandler(AuthTokenException.class)
    public ResponseEntity<ResultData<CodeError>> on(AuthTokenException e) {
        var status = HttpStatus.UNAUTHORIZED;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(ResultData.error(error, false));
    }

    @ExceptionHandler(ConnectionException.class)
    public ResponseEntity<ResultData<CodeError>> on(ConnectionException e) {
        var status = HttpStatus.BAD_GATEWAY;
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
        return ResponseEntity.status(status).body(new ResultData<>(error));
    }

    @ExceptionHandler(NotFormatSuitableException.class)
    public ResponseEntity<ResultData<CodeError>> on(NotFormatSuitableException e) {
        var status = HttpStatus.BAD_REQUEST;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(new ResultData<>(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultData<CodeError>> on(Exception e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logError(e, status);
        CodeError error = new CodeError();
        error.setCode(status.value());
        error.setMessage(e.getMessage());
        return ResponseEntity.status(status).body(new ResultData<>(error));
    }

    private void logError(Exception e, HttpStatus status) {
        log.error("Error: status={} msg={}", status.value(), e.getMessage());
    }
}