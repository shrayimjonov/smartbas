package davr.uzcard.Exception;

import davr.uzcard.Model.ErrorDto;
import org.postgresql.util.PSQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Advice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException e) {
        return getErrorDtoResponseEntity(422, e, "BadRequest");
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<?> pSQLException(PSQLException e) {
        return getErrorDtoResponseEntity(423, e, "SQL error");
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> badRequest(Exception e) {
        return getErrorDtoResponseEntity(424, e, "Wrong Request");
    }

    private static ResponseEntity<ErrorDto> getErrorDtoResponseEntity(int code, Exception e, String detail) {
        return ResponseEntity.ok(new ErrorDto(String.valueOf(code), e.getMessage(), detail));
    }

}
