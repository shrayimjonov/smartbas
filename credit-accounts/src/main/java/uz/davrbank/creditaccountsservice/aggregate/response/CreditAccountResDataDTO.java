package uz.davrbank.creditaccountsservice.aggregate.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditAccountResDataDTO implements Serializable {

    @JsonProperty("BRANCH")
    private String branch;

    @JsonProperty("ANKETA_ID")
    private String anketaId;

    @JsonProperty("ACCOUNT")
    private String account;

    @JsonProperty("ACC_TYPE_ID")
    private Integer accTypeId;

    @JsonProperty("ACC_TYPE_NAME")
    private String accTypeName;

    @JsonProperty("SALDO_OUT")
    private Double saldoOut;

    @JsonProperty("STATE")
    private Integer state;

    @JsonProperty("STATE_NAME")
    private String stateName;
}
