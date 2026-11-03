package uz.davrbank.educontrolservice.exception.condition;

import uz.davrbank.educontrolservice.exception.dto.ErrorCode;

import java.io.Serial;

/**
 * Исключение для ошибок бизнес-валидации.
 * Выбрасывается при нарушении бизнес-правил, некорректных данных и т.д.
 *
 * <p>HTTP статус: 400 Bad Request или 422 Unprocessable Entity</p>
 *
 * @version 1.0.0
 */
public class BusinessValidationException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение с кодом ошибки и сообщением.
     *
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     */
    public BusinessValidationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Создаёт исключение с кодом ошибки (использует сообщение по умолчанию).
     *
     * @param errorCode код ошибки
     */
    public BusinessValidationException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Создаёт исключение с кодом ошибки, сообщением и причиной.
     *
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public BusinessValidationException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Создаёт исключение с сообщением (использует код BAD_REQUEST по умолчанию).
     *
     * @param message сообщение об ошибке
     */
    public BusinessValidationException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}

