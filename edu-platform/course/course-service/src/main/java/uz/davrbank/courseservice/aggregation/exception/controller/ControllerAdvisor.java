package uz.davrbank.courseservice.aggregation.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.davrbank.courseservice.aggregation.exception.condition.BadRequestException;
import uz.davrbank.courseservice.aggregation.exception.condition.ConnectionException;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.exception.condition.ThirdPartyException;


import javax.naming.AuthenticationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerAdvisor.class);


    @ExceptionHandler(ThirdPartyException.class)
    public ResponseEntity<Object> handleFileNotExistException(ThirdPartyException exception) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("code", 204);
        response.put("message", "ДАННЫЕ С СЕРВЕРА НЕ НАЙДЕНЫ");
        response.put("metadata", List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @ExceptionHandler(ConnectionException.class)
    public ResponseEntity<Object> handleGeneralException(ConnectionException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "code", HttpStatus.BAD_GATEWAY.value(),
                        "message", exception.getMessage(),
                        "metadata", List.of("Пожалуйста, попробуйте позже")
                )
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of(
                        "code", HttpStatus.UNAUTHORIZED.value(),
                        "message", exception.getMessage(),
                        "metadata", List.of("Пожалуйста, авторизуйтесь")
                )
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s", error.getDefaultMessage()))
                .toList();
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());
        response.put("message", "Обязательные поля не заполнены");
        response.put("metadata", errors);
        return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(response);
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException exception) {
//        LOG.error("Данные не найдены :{}", exception.getMessage());
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.CONFLICT);
        response.put("message", "Данные не найдены");
        response.put("metadata", List.of(exception.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleDataNotFoundException(BadRequestException exception) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Плохой запрос");
        response.put("metadata", List.of(exception.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }
}
