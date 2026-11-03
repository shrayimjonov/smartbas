package uz.davrbank.accounttocardservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.accounttocardservice.aggregate.enums.TranStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UzCardTransCreditResponseDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("refNum")
    private String refNum;

    @JsonProperty("ext")
    private String ext;

    @JsonProperty("pan")
    private String pan;

    @JsonProperty("expiry")
    private String expiry;

    @JsonProperty("tranType")
    private String tranType;

    @JsonProperty("date7")
    private String date7;

    @JsonProperty("date12")
    private String date12;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("stan")
    private String stan;

    @JsonProperty("field38")
    private String field38;

    @JsonProperty("field48")
    private String field48;

    @JsonProperty("field91")
    private String field91;

    @JsonProperty("merchantId")
    private String merchantId;

    @JsonProperty("terminalId")
    private String terminalId;

    @JsonProperty("resp")
    private String resp;

    @JsonProperty("respText")
    private String respText;

    @JsonProperty("respSV")
    private String respSV;

    @JsonProperty("status")
    private TranStatus status;
}
