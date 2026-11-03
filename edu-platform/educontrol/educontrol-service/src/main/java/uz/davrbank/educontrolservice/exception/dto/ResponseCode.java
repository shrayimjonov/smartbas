package uz.davrbank.educontrolservice.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    GENERAL_ERROR(500, "General error. id=%s"),
    INFORMATION_SUCCESSFULLY_CONFIRM(0, "Данные успешно обработаны"),
    ACCESS_DENIED(1, "Сервис временно не доступен"),
    ERROR_REQUEST_PROCESS(2, "Ошибка при обработке запроса"),
    REQUIRED_DATA_MISSING(451, "Обязательные данные отсутствуют"),
    DATABASE(112, "Возникла проблема в базе данных. Пожалуйста, повторите попытку позже . "),
    DATA_NOT_FOUND(114, "информация недоступна. Пожалуйста, повторите попытку позже . "),
    TOKEN(222, "Возникла проблема с получением токена от третьей стороны. "),
    DATABASE_EMPTY(113, " База данных пуста. "),
    FORMAT_ISSUE(5, "Неправильный формат данных"),
    DUBLICATE_INFO(13, "Имеется дубликат"),
    THIRD_PARTY_EXCEPTION(100, "ошибка третьей стороны"),
    NOT_FORMAT_SUITABLE_EXCEPTION(101, "Exception «Не подходит формат»"),
    CONNECTION_EXCEPTION(102, "Connection Exception to Server");
    private final int code;
    private final String message;
}
