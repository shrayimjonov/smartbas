package uz.davrbank.schotoborotservice.aggregate.dto.integration.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchotoborotGetPageableResponse {

    @JsonProperty("TRANSACTION_ID") //Идентификатор транзакции
    String transactionId;

    @JsonProperty("DOC_NUM")//Номер документа
    String docNum;

    @JsonProperty("D_DATE")//Дата проведения
    String docDate;

    @JsonProperty("KOD_OPER")//Код операции
    String codOper;

    @JsonProperty("DATE_PROV")//Время проведения(АБС)
    String dataProv;

    @JsonProperty("TERMINAL_DATE_OPER")//Дата обработки(Терминал)
    String terminalDateOper;

    @JsonProperty("BANK_CL")//Банк отправителя
    String bankCl;

    @JsonProperty("ACC_CL")//Счёт отправителя
    String accCl;

    @JsonProperty("NAME_CL")//Наименование отправителя
    String nameCl;

    @JsonProperty("BANK_CO")//Банк получателя
    String bankCo;

    @JsonProperty("ACC_CO")//Счёт получателя
    String accCo;

    @JsonProperty("NAME_CO")//Наименование получателя
    String nameCo;

    @JsonProperty("PURPOSE")//Назначение платежа
    String purpose;

    @JsonProperty("SUMMA_DEBET")//Сумма (дебет)
    BigDecimal summaDebit;

    @JsonProperty("SUMMA_CREDIT")//Сумма (кредит)
    BigDecimal summaCredit;

    @JsonProperty("STATE_NAME")//Статус операции
    String stateName;
}
