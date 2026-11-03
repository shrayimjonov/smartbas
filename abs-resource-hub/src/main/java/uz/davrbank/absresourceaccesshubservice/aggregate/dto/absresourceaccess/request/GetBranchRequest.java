package uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetBranchRequest {
    @JsonProperty("iBranchType")
    @NotNull(message = "iBranchType is required")
    @Min(value = 0, message = "iBranchType must be 0 or 1")
    @Max(value = 1, message = "iBranchType must be 0 or 1")
    private Integer iBranchType;
}
