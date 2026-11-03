package uz.davrbank.accounttocardservice.exception.controller;

import liquibase.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.davrbank.accounttocardservice.exception.condition.*;

import java.nio.file.AccessDeniedException;


public class ControlerAdvicer {

    @RestControllerAdvice
    @RequiredArgsConstructor
    public class ControlAdvisor {
        private static final Logger LOG = LoggerFactory.getLogger(ControlAdvisor.class);

        @ExceptionHandler(AccessDeniedException.class)
        ResponseEntity<ErrorResponse> on(AccessDeniedException e) {
            LOG.error("Access Denied : {}", e.getMessage());
            ErrorResponse access = ErrorResponse.of(ResponseCode.ACCESS_DENIED, e.getMessage());
            return ResponseEntity.status(1).body(access);
        }

        @ExceptionHandler(NotFormatSuitableException.class)
        @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
        ResponseEntity<ErrorResponse> on(NotFormatSuitableException e) {
            LOG.error("Not Format Suitable Exception : {}", e.getMessage());
            ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.NOT_FORMAT_SUITABLE_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        @ExceptionHandler(ConnectionException.class)
        @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
        ResponseEntity<ErrorResponse> on(ConnectionException e) {
            LOG.error("Connection Exception : {}", e.getMessage());
            ErrorResponse error = ErrorResponse.of(ResponseCode.CONNECTION_EXCEPTION, e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
        }

        @ExceptionHandler(CustomInternalServerException.class)
        @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
        ResponseEntity<ErrorResponse> on(CustomInternalServerException exception) {
            LOG.error("Custom internal server exception: {}", exception.getMessage());
            ErrorResponse exceptionResult = ErrorResponse.of(ResponseCode.CONNECTION_EXCEPTION, exception.getMessage());
            return new ResponseEntity<>(exceptionResult, HttpStatus.SERVICE_UNAVAILABLE);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
        ResponseEntity<ErrorResponse> on(MethodArgumentNotValidException ex) {
            LOG.error("Client sent the wrong request: {}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.REQUIRED_DATA_MISSING, ex.getBindingResult()
                    .getAllErrors().get(0).getDefaultMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }

        @ExceptionHandler(DatabaseException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        ResponseEntity<ErrorResponse> on(DatabaseException ex) {
            LOG.error("Database Exception: {}", ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.DATABASE, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(AuthTokenException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        ResponseEntity<ErrorResponse> on(AuthTokenException ex) {
            LOG.error("AuthToken Exception: {}", ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.TOKEN, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(GeneralErrorException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        ResponseEntity<ErrorResponse> on(GeneralErrorException ex) {
            LOG.error("GeneralErrorException Exception: {}", ex.getMessage());
            ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.GENERAL_ERROR, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
