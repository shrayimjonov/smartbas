package uz.davrbank.bustreasury.exeption;

import java.io.Serial;


public class FormatIssueException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7034897190745766939L;

    public FormatIssueException(String message) {
        super(message);
    }
}