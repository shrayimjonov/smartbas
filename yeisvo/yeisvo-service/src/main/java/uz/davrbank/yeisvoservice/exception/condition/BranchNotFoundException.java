package uz.davrbank.yeisvoservice.exception.condition;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message) {
        super(message);
    }
}
