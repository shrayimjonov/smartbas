package uz.davrbank.zalogautoservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public class AutoRequestDto implements Serializable {

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("state")
    private Integer state;

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

    @JsonProperty("stateNumber")
    private String stateNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("pageNumber")
    private Integer pageNumber;

    @JsonProperty("pageSize")
    private Integer pageSize;
}