package uz.davrbank.bustreasury.exeption;

import java.io.Serial;

public class ThirdPartyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4470414516966466003L;

    public ThirdPartyException(String message) {
        super(message);
    }
}