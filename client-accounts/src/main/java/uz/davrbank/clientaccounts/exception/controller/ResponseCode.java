package uz.davrbank.clientaccounts.exception.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    GENERAL_ERROR(500, "General error. id=%s"),
    INFORMATION_SUCCESSFULLY_CONFIRM(0, "Данные успешно обработаны."),
    ACCESS_DENIED(1, "Сервис временно не доступен."),
    ERROR_REQUEST_PROCESS(2, "Ошибка при обработке запроса"),
    REQUIRED_DATA_MISSING(451, "Обязательные данные отсутствуют"),
    DATABASE(400, "Возникла проблема в базе данных. Пожалуйста, повторите попытку позже. "),
    DATA_NOT_FOUND(510, "информация не найдена (GAI:третья сторона). Никакое транспортное средство не зависит от PIN."),
    TOKEN(409, "Возникла проблема с получением токена от третьей стороны. "),
    FORMAT_ISSUE(5, "Неправильный формат данных."),
    DUBLICATE_INFO(13, "Имеется дубликат."),
    THIRD_PARTY_EXCEPTION(502, "Ошибка третьей стороны."),
    SUCCESSFUL_CREATE_EXCEPTION(200, "Успешно создать"),
    NOT_FORMAT_SUITABLE_EXCEPTION(422, "Exception «Не подходит формат."),
    CONNECTION_EXCEPTION(503, "Connection Exception to Server.");
    private final int code;
    private final String message;
}
