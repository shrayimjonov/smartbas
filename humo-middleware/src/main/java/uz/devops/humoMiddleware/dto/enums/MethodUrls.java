package uz.devops.humoMiddleware.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MethodUrls {
    GET_REAL_PAN("getRealPan", "/api/getRealPan/test?masked_pan=", "/api/getRealPan?masked_pan="),
    GET_MASKED_PAN("getMaskedPan", "/api/getMaskedPan/test?pan=", "/api/getMaskedPan?pan="),
    GET_CARD_INFO("getCardInfo", "/test/getCardInfo", "/api/getCardInfo"),
    CUSTOMER_ACTIVATE("customerActivate","/test/v2/mb/customer/activate","/v2/mb/customer/activate"),
    CUSTOMER_REMOVE_CARD("customerRemoveCard","/test/v2/mb/customer/remove-card","/v2/mb/customer/remove-card"),
    CUSTOMER_EDIT_CARD("customerEditCard","/test/v2/mb/customer/edit-card","/v2/mb/customer/edit-card"),
    IIA_CS_CARD_V2("iiaCsCard","/test/v2/iiacs/card","/v2/iiacs/card"),
    IIA_CS_CARD_V3("iiaCsCard","/test/v3/iiacs/card","/v3/iiacs/card"),
    CUSTOMER_CARDS_BY_PASSPORT("customerCardsByPassport","/test/cs/v1/customer/cards/by-passport","/cs/v1/customer/cards/by-passport"),
    CUSTOMER_CARDS_BY_PERSON_CODE("customerCardsByPersonCode","/test/cs/v1/customer/cards/by-person-code","/cs/v1/customer/cards/by-person-code"),
    CCR2_EXCHANGE_RATES("ccr2ExchangeRates","/test/v2/ccr2/exchange-rates","/v2/ccr2/exchange-rates"),
    CUSTOMER("customer","/test/v2/mb/customer","/v2/mb/customer"),
    CUSTOMER_LIST("customerList","/test/v2/mb/customer-list","/v2/mb/customer-list"),
    CUSTOMER_DEACTIVATE("customerDeactivate","/test/v2/mb/customer/deactivate","/v2/mb/customer/deactivate"),
    CHANGE_PHONE_NUMBER("changePhoneNumber","/test/v2/mb/customer/change-phone-number","/v2/mb/customer/change-phone-number"),
    CHANGE_MESSAGE_LANG("changeMessageLang","/test/v2/mb/customer/change-cardholders-message-lang","/v2/mb/customer/change-cardholders-message-lang");

    private String methodName;
    private String testUrl;
    private String prodUrl;
}
