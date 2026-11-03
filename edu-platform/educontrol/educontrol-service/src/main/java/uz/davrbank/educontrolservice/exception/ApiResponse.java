package uz.davrbank.educontrolservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Стандартизированная структура ответа API для успешных операций.
 * Используется во всех микросервисах для единообразной обработки успешных ответов.
 *
 * <p>Пример использования:</p>
 * <pre>{@code
 * ApiResponse.of("Данные успешно сохранены", userDto);
 * ApiResponse.of(HttpStatus.CREATED.value(), "Пользователь создан", newUser);
 * }</pre>
 *
 * @param <T> тип данных в ответе
 * @version 1.0.0
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "status", "message", "data", "timestamp"})
public class ApiResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Флаг успешности операции (всегда true для успешных ответов).
     */
    @Builder.Default
    private final boolean success = true;

    /**
     * HTTP статус код ответа.
     */
    private final int status;

    /**
     * Сообщение об успешном выполнении операции на русском языке.
     */
    private final String message;

    /**
     * Краткое описание типа ошибки (например, "Bad Request", "Not Found").
     */
    private final String error;

    /**
     * Данные/полезная нагрузка ответа.
     */
    private final T data;

    /**
     * Временная метка выполнения операции.
     */
    private final LocalDateTime timestamp;

    /**
     * Создает успешный ответ со статусом 200 OK.
     *
     * @param message сообщение об успехе
     * @param data    данные ответа
     * @param <T>     тип данных
     * @return новый экземпляр ApiSuccessResponse
     */
    public static <T> ApiResponse<T> of(String message, T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Создает успешный ответ с указанным статусом.
     *
     * @param status  HTTP статус код
     * @param message сообщение об успехе
     * @param data    данные ответа
     * @param <T>     тип данных
     * @return новый экземпляр ApiSuccessResponse
     */
    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Создает успешный ответ только с данными (статус 200, без сообщения).
     *
     * @param data данные ответа
     * @param <T>  тип данных
     * @return новый экземпляр ApiSuccessResponse
     */
    public static <T> ApiResponse<T> of(T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Создает успешный ответ только с сообщением (без данных).
     *
     * @param message сообщение об успехе
     * @return новый экземпляр ApiSuccessResponse
     */
    public static ApiResponse<Void> of(String message) {
        return ApiResponse.<Void>builder()
                .status(200)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Создает успешный ответ со статусом 201 Created.
     *
     * @param message сообщение об успехе
     * @param data    данные ответа
     * @param <T>     тип данных
     * @return новый экземпляр ApiSuccessResponse
     */
    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .status(201)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Создает ApiResponse с текущей временной меткой.
     *
     * @param status  HTTP статус код
     * @param error   тип ошибки
     * @param message сообщение об ошибке
     * @return новый экземпляр ApiErrorResponse
     */
    public static <T> ApiResponse<T> of(int status, String error, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

}

