package uz.davrbank.partnervalidationservice.validation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GatewayMethod105Response(
        @JsonProperty("ERROR_CODE")
        Integer errorCode,
        @JsonProperty("ERROR_NOTE")
        String errorNote,
        @JsonProperty("TRANS_ID")
        String transId,
        @JsonProperty("CODE_RESIDENT")
        Integer codeResident,
        @JsonProperty("CODE_COUNTRY")
        Integer codeCountry,
        @JsonProperty("SIGN_REGISTR")
        Integer signRegistr,
        @JsonProperty("CODE_FORM")
        String codeForm,
        @JsonProperty("DATE_OPEN")
        String dateOpen,
        @JsonProperty("DATE_CLOSE")
        String dateClose,
        @JsonProperty("STATE")
        Integer state,
        @JsonProperty("CODE_BANK")
        String codeBank,
        @JsonProperty("CODE_CLASS_CREDIT")
        String codeClassCredit,
        @JsonProperty("CODE_CAPACITY")
        String codeCapacity,
        @JsonProperty("PENSION_SERTIF_SERIAL")
        String pensionSertifSerial,
        @JsonProperty("PASSPORT_DATE_EXPIRATION")
        String passportDateExpiration,
        @JsonProperty("CODE_ADR_REGION")
        String codeAdrRegion,
        @JsonProperty("CODE_ADR_DISTR")
        String codeAdrDistr,
        @JsonProperty("INPS")
        String inps,
        @JsonProperty("SIGN_VIP")
        String signVip,
        @JsonProperty("PINFL")
        String pinfl,
        @JsonProperty("DATE_OPEN_NIBBD")
        String dateOpenNibbd,
        @JsonProperty("DATE_CLOSE_NIBBD")
        String dateCloseNibbd,
        @JsonProperty("TYPE_BLACK")
        Integer typeBlack,
        @JsonProperty("BLACK_CODE")
        Integer blackCode,
        @JsonProperty("BLACK_NOTE")
        String blackNote
) {
}
