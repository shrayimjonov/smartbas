package uz.davrbank.journaltransservice.aggregate.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
public class PageableRequestDTO implements Serializable {

    @JsonProperty("iStartDate")
    private String iStartDate;

    @JsonProperty("iEndDate")
    private String iEndDate;

    @JsonProperty("iAccBalKod")
    private String iAccBalKod;

    @JsonProperty("iBranch")
    private String iBranch;

    @JsonProperty("iPageNumber")
    private Integer iPageNumber;

    @JsonProperty("iPageSize")
    private Integer iPageSize;
}
