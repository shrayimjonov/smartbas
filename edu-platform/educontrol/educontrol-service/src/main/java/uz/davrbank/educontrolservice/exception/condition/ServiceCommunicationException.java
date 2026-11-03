package uz.davrbank.educontrolservice.exception.condition;

import uz.davrbank.educontrolservice.exception.dto.ErrorCode;

import java.io.Serial;

/**
 * Исключение для ошибок связи с внешними сервисами.
 * Выбрасывается при недоступности сервиса, таймаутах, ошибках соединения и т.д.
 *
 * <p>HTTP статус: 503 Service Unavailable или 502 Bad Gateway</p>
 *
 * @version 1.0.0
 */
public class ServiceCommunicationException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение с кодом ошибки и сообщением.
     *
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     */
    public ServiceCommunicationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Создаёт исключение с кодом ошибки (использует сообщение по умолчанию).
     *
     * @param errorCode код ошибки
     */
    public ServiceCommunicationException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Создаёт исключение с кодом ошибки, сообщением и причиной.
     *
     * @param errorCode код ошибки
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public ServiceCommunicationException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Создаёт исключение с сообщением (использует код SERVICE_UNAVAILABLE по умолчанию).
     *
     * @param message сообщение об ошибке
     */
    public ServiceCommunicationException(String message) {
        super(ErrorCode.SERVICE_UNAVAILABLE, message);
    }
}

