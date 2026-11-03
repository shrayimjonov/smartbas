package uz.davrbank.zalogipatekahubservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("PK")
    private Integer PK;

    @JsonProperty("BRANCH")
    private String BRANCH;

    @JsonProperty("ID")
    private Integer ID;

    @JsonProperty("ACCOUNT")
    private String ACCOUNT;

    @JsonProperty("ID_CLIENT")
    private String ID_CLIENT;

    @JsonProperty("CADASTRE_TYPE")
    private Integer CADASTRE_TYPE;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("CERTIFICATE_NUM")
    private String CERTIFICATE_NUM;

    @JsonProperty("CERTIFICATE_DATE")
    private LocalDate CERTIFICATE_DATE;

    @JsonProperty("CADASTRE_NUM")
    private String CADASTRE_NUM;

    @JsonProperty("REYESTR_NUM")
    private String REYESTR_NUM;

    @JsonProperty("SQUARE")
    private Integer SQUARE;

    @JsonProperty("OVNERSHIP")
    private Integer OVNERSHIP;

    @JsonProperty("CADASTRE_AMOUNT")
    private Long CADASTRE_AMOUNT;

    @JsonProperty("ESTIMATE_INN")
    private String ESTIMATE_INN;
}