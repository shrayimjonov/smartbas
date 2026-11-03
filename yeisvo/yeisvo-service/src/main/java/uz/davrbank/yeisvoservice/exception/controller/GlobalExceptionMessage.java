package uz.davrbank.yeisvoservice.exception.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.dao.DataAccessException;
import uz.davrbank.yeisvoservice.exception.condition.BranchNotFoundException;
import uz.davrbank.yeisvoservice.exception.condition.DuplicateFieldException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class GlobalExceptionMessage extends RuntimeException {

    @JsonProperty(value = "code")
    private Integer code;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "data")
    private Object data;


    public GlobalExceptionMessage(String message) {
        this.message = message;
    }

}