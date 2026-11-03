package uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BranchDto {
    @JsonProperty("BANK_ID")
    private String bankId;

    @JsonProperty("BANK_NAME")
    private String bankName;
}
