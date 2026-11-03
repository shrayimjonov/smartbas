package uz.davrbank.schotoborotservice.exception.condition;

import lombok.experimental.StandardException;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 15.12.2025
 * Time: 13:29
 * Project: schotoborot-service
 */
@StandardException
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
