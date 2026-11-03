package uz.davrbank.zalogautoservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoDataDto implements Serializable {
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

    @JsonProperty("NAME_ZALOG")
    private String nameZalog;

    @JsonProperty("PINFL_INN_ZALOG")
    private String pinflInnZalog;

    @JsonProperty("ADDRESS_NCI")
    private String addressNci;

    @JsonProperty("CAR_TYPE")
    private String carType;

    @JsonProperty("CAR_DATE_MADE")
    private String carDateMade;

    @JsonProperty("CAR_MARKA")
    private String carMarka;

    @JsonProperty("CAR_MODEL")
    private String carModel;

    @JsonProperty("ENGINE_NUM")
    private String engineNum;

    @JsonProperty("BODY_NUM")
    private String bodyNum;

    @JsonProperty("STATE_NUMBER")
    private String stateNumber;

    @JsonProperty("TEX_PASSPORT")
    private String texPassport;

    @JsonProperty("DOC_DATE")
    private String docDate;

    @JsonProperty("CHASSIS_NUMBER")
    private String chassisNumber;

    @JsonProperty("TRANSPORT_TYPE")
    private String transportType;

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
