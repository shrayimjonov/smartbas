package uz.davrbank.clientaccounts.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientAccountRequestDto {
    @JsonProperty("iDate")
    private String iDate;

    @JsonProperty("iBranch")
    private String iBranch;

    @JsonProperty("iClientType")
    private Integer iClientType;

    @JsonProperty("iState")
    private Integer iState;

    @JsonProperty("iValType")
    private Integer iValType;

    @JsonProperty("iAccList")
    private String iAccList;

    @JsonProperty("iAccount")
    private String iAccount;

    @JsonProperty("iClientId")
    private String iClientId;

    @JsonProperty("iPageNumber")
    private Integer iPageNumber;

    @JsonProperty("iPageSize")
    private Integer iPageSize;

    @JsonProperty("iSort")
    private String iSort;
}