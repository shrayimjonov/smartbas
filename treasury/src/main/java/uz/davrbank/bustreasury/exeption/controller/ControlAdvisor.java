package uz.davrbank.bustreasury.exeption.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.davrbank.bustreasury.exeption.*;

import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControlAdvisor {
    private static final Logger LOG = LoggerFactory.getLogger(ControlAdvisor.class);
    private ErrorResponse errorResponse;

    @ExceptionHandler(AuthTokenException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    ResponseEntity<ErrorResponse> on(AuthTokenException exception) {
        LOG.info("Authentication Exception : {}", exception.getMessage());
        errorResponse = ErrorResponse.of(ResponseCode.NOT_PERMISSION_EXCEPTION, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({ThirdPartyException.class})
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    ResponseEntity<ErrorResponse> on(ThirdPartyException e) {
        LOG.error("Third party exception : {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.THIRD_PARTY_EXCEPTION, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(ConnectionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<ErrorResponse> handleConnectionException(ConnectionException connectionException) {
        LOG.error("Connection Exception : {}", connectionException.getMessage());
        errorResponse = ErrorResponse.of(ResponseCode.CONNECTION_EXCEPTION, connectionException.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<ErrorResponse> handleFormatConflictIssueException(AlreadyExistException alreadyException) {
        LOG.error("Data duplicate is : {}", alreadyException.getMessage());
        errorResponse = ErrorResponse.of(ResponseCode.ALREADY_EXIST_EXCEPTION, alreadyException.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFormatSuitableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ResponseEntity<ErrorResponse> handleNotFormatSuitableException(NotFormatSuitableException notFormatSuitableException) {
        LOG.error("Not Format Suitable Exception : {}", notFormatSuitableException.getMessage());
        errorResponse = ErrorResponse.of(ResponseCode.NOT_FORMAT_SUITABLE_EXCEPTION, notFormatSuitableException.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
    ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException dataNotFoundException) {
        LOG.error("Data Not Found Exception: {}", dataNotFoundException.getMessage());
        errorResponse = ErrorResponse.of(ResponseCode.REQUESTED_RANGE_NOT_SATISFIABLE, dataNotFoundException.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ErrorResponse> handleDataNotFoundException(NotFoundException notFoundException) {
        LOG.error("Data Not Found : {}", notFoundException.getMessage());
        errorResponse = ErrorResponse.of(ResponseCode.DATA_NOT_FOUND_EXCEPTION, notFoundException.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ErrorResponse> handleDatabaseEmptyException(DatabaseEmptyException databaseEmptyException) {
        LOG.error("Database Empty: {}", databaseEmptyException.getMessage());
        errorResponse = ErrorResponse.of(ResponseCode.DATABASE_EMPTY, databaseEmptyException.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FormatIssueException.class)
    @ResponseStatus(HttpStatus.LENGTH_REQUIRED)
    ResponseEntity<ErrorResponse> handleFormatIssueException(FormatIssueException formatIssueException) {
        LOG.error("Format is not suitable: {}", formatIssueException.getMessage());
        errorResponse = ErrorResponse.of(ResponseCode.FORMAT_ISSUE, formatIssueException.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.LENGTH_REQUIRED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOG.error("Validate Exception : {}", ex.getMessage());
        BindingResult bindingResult = ex.getBindingResult();
        String defaultMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.REQUIRED_DATA_MISSING, defaultMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }
}