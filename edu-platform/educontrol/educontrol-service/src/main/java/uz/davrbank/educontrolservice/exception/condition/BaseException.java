package uz.davrbank.educontrolservice.exception.condition;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.davrbank.educontrolservice.exception.dto.ErrorCode;

import java.io.Serial;

/**
 * Базовый класс исключений для всех пользовательских бизнес-исключений.
 * Обеспечивает согласованную обработку ошибок с кодами ошибок и HTTP статусами.
 *
 * <p>Все пользовательские исключения должны наследоваться от этого класса.</p>
 *
 * @version 1.0.0
 */
@Getter
public abstract class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    /**
     * Создаёт исключение с кодом ошибки и сообщением.
     *
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     */
    protected BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    /**
     * Создаёт исключение с кодом ошибки, сообщением и причиной.
     *
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    protected BaseException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    /**
     * Создаёт исключение с кодом ошибки (использует сообщение по умолчанию).
     *
     * @param errorCode код ошибки
     */
    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    /**
     * Создаёт исключение с кодом ошибки и причиной (использует сообщение по умолчанию).
     *
     * @param errorCode код ошибки
     * @param cause причина исключения
     */
    protected BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDefaultMessage(), cause);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

    /**
     * Возвращает имя кода ошибки.
     *
     * @return имя кода ошибки
     */
    public String getErrorCodeName() {
        return errorCode.name();
    }

    /**
     * Возвращает числовой код ошибки.
     *
     * @return числовой код ошибки
     */
    public int getErrorCodeValue() {
        return errorCode.getCode();
    }
}

