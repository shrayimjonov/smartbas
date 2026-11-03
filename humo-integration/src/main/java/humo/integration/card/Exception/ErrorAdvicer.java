package humo.integration.card.Exception;

import feign.RetryableException;
import humo.integration.card.Model.Dto.core.response.ResultData;
import humo.integration.card.Model.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ErrorAdvicer {


    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionMessage on(BadRequestException ignored) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        ExceptionMessage.Error s = new ExceptionMessage.Error();
        s.setMessage(ignored.getMessage());
        s.setCode("400");
        exceptionMessage.setError(s);
        return exceptionMessage;
    }

    @ExceptionHandler(HumoFeignError.class)
    public Map<String, String> connectTimeOut(HumoFeignError out) {
        log.error("Rest advice error: {}", out.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code", "500");
        map.put("message", out.getMessage());
        return map;
    }

    @ExceptionHandler({GatewayException.class, ThirdPartyException.class})
    public ResponseEntity<?> backException(RuntimeException r) {
        log.error("Back throws exception: {}", r.getMessage());
        return ResponseEntity.ok(
                new ResultData<>(
                        -1,
                        r.getMessage()
                )
        );
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<?> cardNotFound(RuntimeException r) {
        log.error("Card info throws exception: {}", r.getMessage());
        return ResponseEntity.ok(
                new ResultData<>(
                        -1,
                        r.getMessage()
                )
        );
    }

    @ExceptionHandler(CardHasNotAccessException.class)
    public ResponseEntity<?> cardHasNotAccessException(RuntimeException r) {
        log.error("Card has not access throws exception: {}", r.getMessage());
        return ResponseEntity.ok(
                new ResultData<>(
                        403,
                        r.getMessage()
                )
        );
    }



    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<?> retryableException(RetryableException out) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "500");
        map.put("message", out.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(HandleHumoError.class)
    public ResponseEntity<?> handleHumoError(HandleHumoError out) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "403");
        map.put("message", out.getMessage());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ExceptionHandler(DateParseException.class)
    public ResponseEntity<?> handleDateError(DateParseException out) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "403");
        map.put("message", out.getMessage());
        return ResponseEntity.ok(map);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException out) {

        Map<String, String> map = new HashMap<>();
        map.put("code", "400");
        map.put("message", out.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(VisaException.class)
    public ResponseEntity<?> visaHandleError(VisaException e) {
        log.info("Visa handler error: {}", e.getMessage());


        Map<String, String> map = new HashMap<>();
        map.put("code", "409");
        map.put("message", e.getMessage());

        Map<String, Object> errorDto = new HashMap<>();
        errorDto.put("error", map);
        return new ResponseEntity<>(errorDto, HttpStatus.OK);
    }

    @ExceptionHandler(ScoringException.class)
    public ResponseEntity<?> scoringException(RuntimeException r) {
        log.error("Scoring throws exception: {}", r.getMessage());
        return ResponseEntity.ok(
                new ExceptionMessage(5004, r.getMessage())
        );

    }
}
