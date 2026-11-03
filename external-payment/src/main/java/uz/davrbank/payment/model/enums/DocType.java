package uz.davrbank.payment.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum DocType {
    SIMPLE_PAYMENT("01", "Платежное поручение"),
    PAYMENT_REQUIREMENT("02", "Платежное требование"),
    LIMITED_CHECK("04", "Лимитированные чеки"),
    ACC_APP("05", "Заявление на аккредитив"),
    MEMORIAL("06", "Мемориальный ордер"),
    CORRECT_MEMORIAL("08", "Исправительный мемориальный ор"),
    E_CREDIT_CARD("09", "Электронная кредитная карточка"),
    COLLECTION_ORDER("11", "Инкассовое поручение"),
    SIMPLE_REQUIREMENT_PAYMENT("12", "Платежное требование поручение"),
    SETTLE_CHECK_BANK("12", "Расчетные чеки коммерческих банков"),
    DBO_PAYMENT("21", "Пл.поруч.через сист.дист.обсл."),
    E_PAYMENT_REQUIREMENT("22", "Электрон. платежное требование"),
    BUDGET_PAYMENT("99", "Платежное поручение бюджет"),
    CASH_OUT_GOING_RECEIPT_ORDER("03", "Расходный кассовый ордер"),
    CASH_INCOMING_RECEIPT_ORDER("93", "Приходный кассовый ордер"),

    ;
    private final String code;
    private final String description;
}
