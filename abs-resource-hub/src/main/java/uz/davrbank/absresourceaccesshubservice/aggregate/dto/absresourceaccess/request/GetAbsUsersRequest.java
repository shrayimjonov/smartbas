package uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAbsUsersRequest {
    @JsonProperty("iBranch")
    @NotBlank(message = "iBranch is required")
    private String iBranch;
}
