package uz.davrbank.filescloudservice.aggrecation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ResultState {
    IncorrectProcessing("This type processing not serviced", 7021),
    PhoneNotFound("Phone number of card not taken or not turned on", 7022),
    SMSNotSent("Problem with sms center", 7023),
    CardNotFound("Card not found!", 7024),
    DataNotFound("Data not found!", 7025),
    EntityNotFound("Entity not found!", 7026),
    InternalServerError("In Back something went wrong", 7027),
    SQLException("Database process error", 7028),
    WrongParams("Field or value filled incorrectly!", 7029),
    ThirdPartyError("Third Party connection error...", 7030),
    InvalidRequest("Error caused by incorrect parameters", 7031),
    TimeOutError("Connection time out", 1032),
    NotProcessed("The operation was not processed to completion.", 7033),
    BlockOperation("This operation is blocked", 7034),
    UnknownError("Unknown error", 7035),
    MissingDate("Missing date", 7036),
    Success("Success", 200),
    InvalidInput("Invalid input", 1035),
    Pending("Please a wait", -1);

    private final String message;
    private final Integer code;
}
