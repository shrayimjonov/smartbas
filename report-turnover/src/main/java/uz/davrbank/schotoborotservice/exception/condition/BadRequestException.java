package uz.davrbank.schotoborotservice.exception.condition;

import lombok.experimental.StandardException;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 15.12.2025
 * Time: 13:29
 * Project: schotoborot-service
 */
@StandardException
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
