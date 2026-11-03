package uz.davrbank.educontrolservice.exception.condition;

import uz.davrbank.educontrolservice.exception.dto.ErrorCode;

import java.io.Serial;

/**
 * Исключение для ошибок аутентификации и авторизации.
 * Выбрасывается при недействительных учётных данных, истёкших токенах и т.д.
 *
 * <p>HTTP статус: 401 Unauthorized</p>
 *
 * @version 1.0.0
 */
public class UnauthorizedException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение с кодом ошибки и сообщением.
     *
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     */
    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Создаёт исключение с кодом ошибки (использует сообщение по умолчанию).
     *
     * @param errorCode код ошибки
     */
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Создаёт исключение с кодом ошибки, сообщением и причиной.
     *
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public UnauthorizedException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Создаёт исключение с сообщением (использует код UNAUTHORIZED по умолчанию).
     *
     * @param message сообщение об ошибке
     */
    public UnauthorizedException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }
}

