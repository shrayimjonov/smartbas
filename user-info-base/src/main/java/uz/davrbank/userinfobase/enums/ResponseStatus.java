package uz.davrbank.userinfobase.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ResponseStatus {
    INCORRECT_DATA(500, "Provided data is incorrect"),
    LANGUAGE_DATA(500, "LanguageType should not be empty!!!"),
    TEXT_DATA(500, "TEXT should not be empty!!!"),
    SUCCESS(200, "SUCCESS"),
    SCHEDULED(200, "MESSAGE SCHEDULED!!!"),
    FAILED(400, "FAILED"),
    INVALID_REQUEST(500, "INVALID REQUEST"),
    NCI_ERROR(500, "NCI ERROR"),
    ELMA_ERROR(500, "ELMA ERROR"),
    FEIGN_ERROR(501, "Third party does not respond"),
    DAVR_MOBILE_ERROR(501, "Davr mobile does not respond"),
    Dochub_Error(20, "Dochub_Error does not respond"),
    Minio_Error(20,"Minio does not respond!"),
    File_Error(20,"File does not respond!"),
    WRONG_REQUEST(21, "Wrong request"),
    USER_NOT_FOUND_EXCEPTION(22, "Could not find!");
    private final Integer code;
    private final String message;
}
