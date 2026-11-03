package uz.davrbank.zalogautohubservice.aggregate.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataDTO implements Serializable {

    @JsonProperty("BRANCH")
    private String branch;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("ACCOUNT")
    private String account;

    @JsonProperty("ID_CLIENT")
    private String idClient;

    @JsonProperty("NAME")
    private String name;

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

    @JsonProperty("DOC_SER_NUM")
    private String docSerNum;

    @JsonProperty("DOC_DATE")
    private String docDate;

    @JsonProperty("CHASSIS_NUMBER")
    private String chassisNumber;

    @JsonProperty("TRANSPORT_TYPE")
    private String transportType;
}
