package uz.davrbank.exception.condition;

import lombok.experimental.StandardException;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 25.11.2025
 * Time: 11:52
 * Project: admin-jobs
 */
@StandardException
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
