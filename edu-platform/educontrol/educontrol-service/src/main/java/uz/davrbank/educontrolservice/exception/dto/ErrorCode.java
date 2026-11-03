package uz.davrbank.educontrolservice.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import uz.davrbank.educontrolservice.exception.condition.BaseException;

/**
 * Перечисление кодов ошибок для стандартизированной обработки исключений.
 * Каждый код ошибки содержит числовой код, HTTP статус и сообщение по умолчанию на русском языке.
 *
 * <p>Используется совместно с {@link BaseException} и его наследниками.</p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Ошибки аутентификации (1xxx)
    UNAUTHORIZED(1001, HttpStatus.UNAUTHORIZED, "Неавторизованный доступ"),
    TOKEN_INVALID(1002, HttpStatus.UNAUTHORIZED, "Недействительный токен"),
    TOKEN_EXPIRED(1003, HttpStatus.UNAUTHORIZED, "Срок действия токена истёк"),
    CREDENTIALS_INVALID(1004, HttpStatus.UNAUTHORIZED, "Неверные учётные данные"),
    ACCESS_DENIED(1005, HttpStatus.FORBIDDEN, "Доступ запрещён"),

    // Ошибки валидации (2xxx)
    BAD_REQUEST(2001, HttpStatus.BAD_REQUEST, "Некорректный запрос"),
    VALIDATION_ERROR(2002, HttpStatus.BAD_REQUEST, "Ошибка валидации данных"),
    REQUIRED_FIELD_MISSING(2003, HttpStatus.BAD_REQUEST, "Обязательное поле отсутствует"),
    INVALID_FORMAT(2004, HttpStatus.BAD_REQUEST, "Неверный формат данных"),

    // Ошибки данных (3xxx)
    NOT_FOUND(3001, HttpStatus.NOT_FOUND, "Данные не найдены"),
    DUPLICATE_ENTRY(3002, HttpStatus.CONFLICT, "Запись уже существует"),
    DATA_INTEGRITY_ERROR(3003, HttpStatus.CONFLICT, "Ошибка целостности данных"),

    // Ошибки сервиса (4xxx)
    SERVICE_UNAVAILABLE(4001, HttpStatus.SERVICE_UNAVAILABLE, "Сервис временно недоступен"),
    EXTERNAL_SERVICE_ERROR(4002, HttpStatus.BAD_GATEWAY, "Ошибка внешнего сервиса"),
    TIMEOUT(4003, HttpStatus.GATEWAY_TIMEOUT, "Превышено время ожидания"),

    // Внутренние ошибки (5xxx)
    INTERNAL_ERROR(5001, HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера"),
    DATABASE_ERROR(5002, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка базы данных"),
    CONFIGURATION_ERROR(5003, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка конфигурации");

    private final int code;
    private final HttpStatus httpStatus;
    private final String defaultMessage;
}

