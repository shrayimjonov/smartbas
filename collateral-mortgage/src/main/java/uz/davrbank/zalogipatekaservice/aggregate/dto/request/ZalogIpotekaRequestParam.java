package uz.davrbank.zalogipatekaservice.aggregate.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZalogIpotekaRequestParam implements Serializable {

    @JsonProperty("startDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private String startDate;

    @JsonProperty("endDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private String endDate;

    @JsonProperty("cadastreType")
    private Integer cadastreType;

    @JsonProperty("state")
    private Integer state;

    @JsonProperty("pageNumber")
    private Integer page;

    @JsonProperty("pageSize")
    private Integer size;

    @JsonProperty("sort")
    private String sort;

    // NEW PARAMETERS
    @JsonProperty("branch")
    private String branch;

    @JsonProperty("loanId")
    private Integer loanId;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("pinfl")
    private String pinfl;

    @JsonProperty("clientName")
    private String clientName;

    @JsonProperty("cadastreNum")
    private String cadastreNum;
}
