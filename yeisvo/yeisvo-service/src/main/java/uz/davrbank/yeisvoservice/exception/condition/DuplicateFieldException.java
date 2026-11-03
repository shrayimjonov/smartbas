package uz.davrbank.yeisvoservice.exception.condition;

public class DuplicateFieldException extends RuntimeException {
    public DuplicateFieldException(String message) {
        super(message);
    }
}