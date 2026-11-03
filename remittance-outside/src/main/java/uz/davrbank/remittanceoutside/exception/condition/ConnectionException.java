package uz.davrbank.remittanceoutside.exception.condition;

import lombok.experimental.StandardException;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 18.11.2025
 * Time: 10:59
 * Project: remittanceoutside
 */
@StandardException
public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
}
