package nci.billing.exception;


import lombok.extern.slf4j.Slf4j;
import nci.billing.model.dto.ProblemDto;
import nci.billing.model.enums.CodeStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class Advice {

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<?> badRequest(BadRequest exception) {
        return badRequestError(exception, CodeStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClosedLoan.class)
    public ResponseEntity<?> closedLoan(ClosedLoan exception) {
        return badRequestError(exception, CodeStatus.LOAN_NOT_FOUND);
    }

    @ExceptionHandler(UnknownType.class)
    public ResponseEntity<?> unknownType(UnknownType exception) {
        return badRequestError(exception, CodeStatus.INVALID_PARAMS);
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<?> notFound(NotFound exception) {
        return badRequestError(exception, CodeStatus.NOT_FOUND);
    }

//    @ExceptionHandler(InternalError.class)
//    public ResponseEntity<?> internalError(InternalError exception) {
//        return internalServerError(exception, CodeStatus.INTERNAL_ERROR);
//    }

    @ExceptionHandler(FraudStopper.class)
    public ResponseEntity<?> fraudStopper(FraudStopper exception) {
        return badRequestError(exception, CodeStatus.FRAUD_TRANSACTION);
    }

    @ExceptionHandler(TimeOut.class)
    public ResponseEntity<?> timeOut(TimeOut exception) {
        return internalServerError(exception, CodeStatus.TIME_OUT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return badRequestError(exception, CodeStatus.INVALID_PARAMS);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> HttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return badRequestError(exception, CodeStatus.WRONG_REQUEST);
    }

    @ExceptionHandler(SimulateThrow.class)
    public ResponseEntity<?> simulateThrow(SimulateThrow exception) {
        return internalServerError(exception, CodeStatus.SIMULATE_ERROR);
    }

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<?> entityNotFound(EntityNotFound exception) {
        return internalServerError(exception, CodeStatus.ENTITY_NOT_FOUND);
    }


    private static ResponseEntity<?> internalServerError(Exception exception, CodeStatus status) {
        log.error("Code Status: {}", status);
        log.error("Internal server error: {}", exception.getMessage());
        return ResponseEntity.ok()
                .body(Map.of("error", ProblemDto.builder()
                        .code(status.getCode())
                        .detail(exception.getLocalizedMessage())
                        .message(exception.getMessage())
                        .build()));
    }

    private static ResponseEntity<?> badRequestError(Exception exception, CodeStatus status) {
        log.info("Code Status: {}", status);
        log.info("Internal server error: {}", exception.getMessage());
        return ResponseEntity.ok().body(Map.of("error", ProblemDto.builder()
                .code(status.getCode())
                .detail(exception.getLocalizedMessage())
                .message(exception.getMessage())
                .build()));
    }


}
