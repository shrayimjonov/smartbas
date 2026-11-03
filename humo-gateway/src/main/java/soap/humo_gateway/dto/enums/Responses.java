package soap.humo_gateway.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Responses {


    CardTypeInCorrect(427,"Card type is incorrect"),
    ArgumentNotValid(428,"Required fields not found or filled incorrectly!"),
  //  ValueTypeNotValid(429,"This type of enum not found"),
    ConnectionError(505,"Connection error"),
    GatewayError(509,"Gateway error"),
    ResponseError(510,"Response error from Humo"),
    EnumTypeNotValid(511,"This type of enum not found");


    private final int code;
    private final String message;

}
