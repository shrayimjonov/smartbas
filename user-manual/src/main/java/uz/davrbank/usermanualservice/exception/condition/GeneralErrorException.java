package uz.davrbank.usermanualservice.exception.condition;

import lombok.experimental.StandardException;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 25.11.2025
 * Time: 11:52
 * Project: usermanual-service
 */
@StandardException
public class GeneralErrorException extends RuntimeException {
    public GeneralErrorException(String message) {
        super(message);
    }
}
