package uz.davrbank.remittanceoutside.exception.condition;

import lombok.experimental.StandardException;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 18.11.2025
 * Time: 10:59
 * Project: remittanceoutside
 */
@StandardException
public class CustomInternalServerException extends RuntimeException {
    public CustomInternalServerException(String message) {
        super(message);
    }
}
