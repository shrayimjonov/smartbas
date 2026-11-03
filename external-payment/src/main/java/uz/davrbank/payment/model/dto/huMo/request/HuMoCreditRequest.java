package uz.davrbank.payment.model.dto.huMo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nodir.commondto.utils.CardUtils;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HuMoCreditRequest {
    @JsonProperty("refNum")
    private String refNum;
    @JsonProperty("pan2")
    private String pan2;
    @JsonProperty("expiry")
    private String expiry;
    @JsonProperty("amount")
    private Long amount;
    @JsonProperty("merchantId")
    private String merchantId;
    @JsonProperty("terminalId")
    private String terminalId;
    @JsonProperty("login")
    private String login;
    @JsonProperty("subSystem")
    private String subSystem;
    @JsonProperty("params")
    private Params params;

    public Map<Object, Object> toMap() {
        return Map.of(
                "refNum", this.refNum,
                "pan", CardUtils.maskPanDigits(this.pan2),
                "expiry", this.expiry,
                "amount", this.amount,
                "merchantId", this.merchantId,
                "terminalId", this.terminalId,
                "login", this.login,
                "subSystem", this.subSystem,
                "params", this.params
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Params {
        @JsonProperty("senderDocType")
        private String senderDocType;
        @JsonProperty("senderSerialNo")
        private String senderSerialNo;
        @JsonProperty("senderIdCard")
        private String senderIdCard;
        @JsonProperty("senderSurname")
        private String senderSurname;
        @JsonProperty("senderFirstName")
        private String senderFirstName;
        @JsonProperty("senderMiddleName")
        private String senderMiddleName;
        @JsonProperty("senderNationality")
        private String senderNationality;
        @JsonProperty("senderDocValidThrough")
        private String senderDocValidThrough; //yyyy-MM-dd
        @JsonProperty("senderPersonCode")
        private String senderPersonCode;
        @JsonProperty("senderBirthDate")
        private String senderBirthDate; //yyyy-MM-dd
    }
}
