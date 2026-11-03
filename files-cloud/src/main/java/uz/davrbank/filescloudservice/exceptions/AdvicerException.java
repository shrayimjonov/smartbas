package uz.davrbank.filescloudservice.exceptions;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import uz.davrbank.filescloudservice.aggrecation.dto.response.ResultData;
import uz.davrbank.filescloudservice.aggrecation.enums.ResultState;

import java.sql.SQLException;
import java.time.DateTimeException;


@RequiredArgsConstructor
@RestControllerAdvice
public class AdvicerException {

    private static final Logger LOG = LoggerFactory.getLogger(AdvicerException.class);

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<?> badRequestErrors(Exception e) {
        LOG.info("Web error: {}", e.getMessage());
        return ResponseEntity.ok(new ResultData<>(ResultState.WrongParams, "Bad request or fields wrong filled!"));
    }


    public ResponseEntity<ResultData<?>> handleBadRequest(Exception e) {
        LOG.warn("Bad request error: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResultData<>(ResultState.WrongParams, "Bad request or fields wrongly filled!"));
    }

    @ExceptionHandler(value = {
            DataNotFoundException.class,
            DateTimeException.class,
            IncorrectDataException.class,
            ThirdPartyException.class,
            SQLException.class,
            GeneralErrorException.class,
            APIException.class,
            IncorrectDataException.class,
            NotImplementedException.class
    })
    public ResponseEntity<ResultData<?>> handleApplicationErrors(Exception e) {
        LOG.warn("Handled app error: {} - {}", e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResultData<>(ResultState.InvalidRequest, e.getMessage()));
    }
}
