package uz.davrbank.payment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.nodir.commondto.exception.*;
import uz.nodir.commondto.model.dto.core.response.ResultData;
import uz.nodir.commondto.model.enums.ResultState;

@RestControllerAdvice
@Slf4j
public class Advice {

    @ExceptionHandler(value = {GatewayException.class, TimeOutException.class, GeneralErrorException.class})
    public ResponseEntity<?> thirdPartyErrors(RuntimeException e) {
        log.info("Rest advice catch error: {}", e.getMessage());
        return ResponseEntity.ok(new ResultData<>(ResultState.ThirdPartyError, e.getMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> badRequestErrors(Exception e) {
        log.info("Web error: {}", e.getMessage());
        return ResponseEntity.ok(new ResultData<>(ResultState.BadRequest, "Bad request or fields wrong filled!"));
    }

    @ExceptionHandler(value = TransactionException.class)
    public ResponseEntity<?> transactionException(RuntimeException e) {
        log.info("Back error: {}", e.getMessage());
        return ResponseEntity.ok(new ResultData<>(ResultState.WrongParams, "Database SQL error!"));
    }


    @ExceptionHandler(value = {InvalidDataException.class, CardException.class, EntityNotFoundException.class, ReverseException.class})
    public ResponseEntity<?> backErrors(RuntimeException e) {
        log.info("Rest advice catch error: {}", e.getMessage());
        return ResponseEntity.ok(new ResultData<>(ResultState.DataNotFound, e.getMessage()));
    }
}
