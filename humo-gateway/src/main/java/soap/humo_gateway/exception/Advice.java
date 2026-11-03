package soap.humo_gateway.exception;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import soap.humo_gateway.dto.ErrorDto;
import soap.humo_gateway.dto.enums.Responses;
import soap.humo_gateway.dto.response.ResponseData;
import soap.humo_gateway.dto.response.ResponseObject;
import soap.humo_gateway.service.ErrorListService;

import javax.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class Advice {
    private final ErrorListService errorListService;

    @ExceptionHandler(ConnectionError.class)
    public ResponseEntity<?> connectionError(ConnectionError ex) {
        return returnResultDate(ex, Responses.ConnectionError);
    }

    @ExceptionHandler(CardTypeInCorrect.class)
    public ResponseEntity<?> cardTypeInCorrect(CardTypeInCorrect ex) {
        return returnBadGatewayError(ex, Responses.CardTypeInCorrect);
    }

    @ExceptionHandler(GatewayError.class)
    public ResponseEntity<?> gatewayError(GatewayError ex) {
        return returnBadGatewayError(ex, Responses.GatewayError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return returnBadRequestError(ex, Responses.ArgumentNotValid);
    }

    @ExceptionHandler(HumoErrorResponse.class)
    public ResponseEntity<?> humoErrorResponse(HumoErrorResponse ex) {
        return returnResultDate(ex, Responses.ResponseError);
    }

    @ExceptionHandler(InvalidData.class)
    public ResponseEntity<?> invalidData(InvalidData ex) {
        return returnResultDate(ex, Responses.ArgumentNotValid);
    }

    @ExceptionHandler(ErrorWithDesc.class)
    public ResponseEntity<?> errorWithDesc(ErrorWithDesc ex) {
        return ResponseEntity.internalServerError().body(ErrorDto.builder()
                .error(ErrorDto.ErrorResponse.builder()
                        .code(Responses.ResponseError.getCode())
                        .message(ex.getMessage())
                        .details(errorListService.findErrorByCode(ex.getMessage()))
                        .build())
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("httpMessageNotReadableException exception: {}", ex.getMessage());
        return ResponseEntity.ok().body(ErrorDto.builder()
                .error(ErrorDto.ErrorResponse.builder()
                        .code(Responses.EnumTypeNotValid.getCode())
                        .message("Fields wrong filled or maybe not presented")
                        //    .details(errorListService.findErrorByCode(ex.getMessage()))
                        .build())
                .build());
    }

//    @ExceptionHandler(UnexpectedTypeException.class)
//    public ResponseEntity<?> unexpectedTypeException(UnexpectedTypeException ex) {
//        return returnBadGatewayError(ex, Responses.EnumTypeNotValid);
//    }


    public ResponseEntity<ErrorDto> returnBadRequestError(Exception ex, Responses responses) {
        log.error("bad Request error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ErrorDto.builder()
                .error(ErrorDto.ErrorResponse.builder()
                        .code(responses.getCode())
                        .message(responses.getMessage())
                        .build())
                .build());
    }

    public ResponseEntity<ErrorDto> returnBadGatewayError(Exception ex, Responses responses) {
        log.error("internal Error error: {}", ex.getMessage());

        return ResponseEntity.ok().body(ErrorDto.builder()
                .error(ErrorDto.ErrorResponse.builder()
                        .code(responses.getCode())
                        .message(responses.getMessage())
                        .details(ex.getMessage())
                        .build())
                .build());
    }

    public ResponseEntity<?> returnResultDate(Exception e, Responses response) {
        return ResponseEntity.ok(new ResponseData<>(false, (long) response.getCode(), e.getMessage()));
    }


}
