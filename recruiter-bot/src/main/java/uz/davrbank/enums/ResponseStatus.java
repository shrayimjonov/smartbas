package uz.davrbank.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ResponseStatus {
    INCORRECT_DATA(500, "Provided data is incorrect"),
    LANGUAGE_DATA(500, "LanguageType should not be empty!!!"),
    SEARCH_TYPE_DATA(500, "SearchType should not be empty!!!"),
    TEXT_DATA(500, "TEXT should not be empty!!!"),
    SUCCESS(200, "SUCCESS"),
    SCHEDULED(200, "MESSAGE SCHEDULED!!!"),
    FAILED(400, "FAILED"),
    BAD_REQUEST(400, "BAD REQUEST"),
    NOT_FOUND(404, "NOT FOUND"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    INVALID_REQUEST(500, "INVALID REQUEST");
    private final Integer code;
    private final String message;
}
