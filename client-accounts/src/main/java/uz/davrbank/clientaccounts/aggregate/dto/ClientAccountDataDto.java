package uz.davrbank.clientaccounts.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAccountDataDto {
    @JsonProperty("BRANCH")
    private String branch;

    @JsonProperty("ID_CLIENT")
    private String idClient;

    @JsonProperty("ACC_NAME")
    private String accName;

    @JsonProperty("PINFL_INN")
    private String pinflInn;

    @JsonProperty("ACCOUNT")
    private String account;

    @JsonProperty("SALDO_NOM")
    private Double saldoNom;

    @JsonProperty("SALDO_EQV")
    private Double saldoEqv;

    @JsonProperty("STATE")
    private String state;
}