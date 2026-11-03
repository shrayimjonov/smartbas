package uz.davrbank.zalogipatekaservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataDTO implements Serializable {

    @JsonProperty("PK")
    private Integer pk;

    @JsonProperty("BRANCH")
    private String branch;

    @JsonProperty("ID_ANKETA")
    private Integer idAnketa;

    @JsonProperty("ACCOUNT")
    private String account;

    @JsonProperty("ID_CLIENT_ZAEM")
    private String idClientZaem;

    @JsonProperty("FIO_ZAEM")
    private String fioZaem;

    @JsonProperty("PINFL_INN_ZAEM")
    private String pinflInnZaem;

    @JsonProperty("ID_CLIENT_ZALOG")
    private String idClientZalog;

    @JsonProperty("FIO_ZALOG")
    private String fioZalog;

    @JsonProperty("PINFL_INN_ZALOG")
    private String pinflInnZalog;

    @JsonProperty("ADDRESS_NCI")
    private String addressNci;

    @JsonProperty("CERTIFICATE_NUM")
    private String certificateNum;

    @JsonProperty("CERTIFICATE_DATE")
    private String certificateDate;

    @JsonProperty("CADASTRE_TYPE")
    private Integer cadastreType;

    @JsonProperty("CADASTRE_NUM")
    private String cadastreNum;

    @JsonProperty("REYESTR_NUM")
    private String reystrNum;

    @JsonProperty("SQUARE")
    private Long square;

    @JsonProperty("OVNERSHIP")
    private Long ownership;

    @JsonProperty("CADASTRE_AMOUNT")
    private Long cadastreAmount;

    @JsonProperty("ESTIMATE_INN")
    private String estimateInn;

    @JsonProperty("DOC_DATE")
    private String docDate;

    @JsonProperty("GUAR_ID")
    private String guarId;

    @JsonProperty("GUAR_ADDRESS")
    private String guarAddress;

    @JsonProperty("GUAR_AMOUNT")
    private Long guarAmount;

    @JsonProperty("GUAR_CURR")
    private String guarCurr;

    @JsonProperty("STATUS")
    private String status;

}