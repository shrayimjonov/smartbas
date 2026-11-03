package uz.davrbank.bustreasury.exeption.controller;

import lombok.Getter;

@Getter
public enum ResponseCode {

    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    GENERAL_ERROR(500, "General error. id=%s"),
    INFORMATION_SUCCESSFULLY_CONFIRM(0, "Данные успешно обработаны"),
    ACCESS_DENIED(1, "Сервис временно не доступен"),
    ERROR_REQUEST_PROCESS(2, "Ошибка при обработке запроса"),
    REQUIRED_DATA_MISSING(451, "Обязательные данные отсутствуют"),
    FORMAT_ISSUE(411, "Неправильный формат данных"),
    DUBLICATE_INFO(13, "Имеется дубликат"),
    THIRD_PARTY_EXCEPTION(100, "ошибка третьей стороны"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Данные не найдены в базе данных. Проверьте введенную информацию."),
    NOT_FORMAT_SUITABLE_EXCEPTION(101, "Exception «Не подходит формат»"),
    SUCCESSFUL_CREATE_EXCEPTION(200, "успешно сохранено в базе данных"),
    SUCCESSFUL_DELETE_EXCEPTION(200, "успешно удалит в базе данных"),
    CONNECTION_EXCEPTION(102, "Connection Exception to Server"),
    DATA_NOT_FOUND_EXCEPTION(20901, "Данные не найдены"),
    RECORD_ALREADY_EXIST_EXCEPTION(20398, "Такая запись уже есть"),
    NOT_PERMISSION_EXCEPTION(20357, "У Вас нет полномочий, получать токен"),
    NOT_ENOUGH_PRIVILEGES_EXCEPTION(20412, "Не достаточно привилегий"),
    REQUIRED_EXCEPTION(20408, "Является обязательным"),
    ONLY_ONE_EXCEPTION(20414, "Может быть только одно"),
    ERROR_DOCUMENT_ISSUE_DATE_EXCEPTION(20320, "Ошибка в реквизите <Дата выдачи документа>"),
    DATABASE_EMPTY(302, " База данных пуста. "),
    ERROR_TAX_COMMITTEE_EXCEPTION(20415, "Ошибка при проверке в ГНК"),
    ERROR_CHECK_NIBBD_EXCEPTION(20416, "Ошибка при проверке в НИББД"),
    ALREADY_EXIST_EXCEPTION(409, "уже существует в базе данных"),
    REQUIRED_ATTRIBUTE_EXCEPTION(20409, "Является обязательным: [Наименование реквизита]"),
    LESS_THAN_EXPECTED_LENGTH_EXCEPTION(20410, "Меньше ожидаемой длины: [Наименование реквизита]"),
    MORE_THAN_EXPECTED_LENGTH_EXCEPTION(20411, "Больше ожидаемой длины: [Наименование реквизита]"),
    INCONSISTENCY_REFERENCE_BOOK_EXCEPTION(20123, "Не соответствие справочнику"),
    DOCUMENT_ATTACHMENT_PROHIBITED_EXCEPTION(20914, "Прикрепление документа запрещается"),
    DEBTOR_CONSENT_REQUIRED_EXCEPTION(20413, "Обязательно должно быть согласия должника");
    public final Integer code;
    public final String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
