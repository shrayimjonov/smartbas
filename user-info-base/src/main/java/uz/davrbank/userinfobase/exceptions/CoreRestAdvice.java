package uz.davrbank.userinfobase.exceptions;

import jakarta.validation.ConstraintDefinitionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.enums.ResponseStatus;


@RestControllerAdvice
@Slf4j
public class CoreRestAdvice {
    @ExceptionHandler({NciException.class})
    public ResponseEntity<?> handleNCIException(NciException exception) {
        return getBaseResponseResponseEntity(ResponseStatus.NCI_ERROR, exception);
    }

    @ExceptionHandler({ElmaException.class})
    public ResponseEntity<?> handleElmaException(ElmaException exception) {
        return getBaseResponseResponseEntity(ResponseStatus.ELMA_ERROR, exception);
    }

    @ExceptionHandler({DavrMobileException.class})
    public ResponseEntity<?> handleDavrMobileException(DavrMobileException exception) {
        return getBaseResponseResponseEntity(ResponseStatus.DAVR_MOBILE_ERROR, exception);
    }

    @ExceptionHandler({DavrhubGatewayException.class})
    public ResponseEntity<?> handleDavrhubGatewayException(DavrhubGatewayException exception) {
        return getBaseResponseResponseEntity(ResponseStatus.FAILED, exception);
    }

    @ExceptionHandler({GeneralException.class})
    public ResponseEntity<?> handleGeneralException(GeneralException exception) {
        return getBaseResponseResponseEntity(ResponseStatus.FAILED, exception);
    }

    @ExceptionHandler({DochubException.class})
    public ResponseEntity<?> handleDochubException(DochubException exception) {
        return getBaseResponseResponseEntity(ResponseStatus.Dochub_Error, exception);
    }

    @ExceptionHandler({MinioException.class})
    public ResponseEntity<?> handleMinioException(MinioException exception) {
        return getBaseResponseResponseEntity(ResponseStatus.Minio_Error, exception);
    }

    @ExceptionHandler({FileException.class})
    public ResponseEntity<?> handleFileException(FileException exception) {
        return getBaseResponseResponseEntity(ResponseStatus.File_Error, exception);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(UsernameNotFoundException exception) {
        return getBaseResponseResponseEntity(ResponseStatus.USER_NOT_FOUND_EXCEPTION, exception);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, ConstraintDefinitionException.class,
            MethodArgumentNotValidException.class})
    public ResponseEntity<?> HttpMessageNotReadableException(Exception exception) {
        return getBaseResponseResponseEntity(ResponseStatus.WRONG_REQUEST, exception);
    }

    private ResponseEntity<?> getBaseResponseResponseEntity(ResponseStatus status, Exception exception) {
        log.error("Rest Advice Error handler exception: {}", exception.getMessage());
        var response = new ResultData<>(false, status, exception);
        return ResponseEntity.ok(response);
    }
}
