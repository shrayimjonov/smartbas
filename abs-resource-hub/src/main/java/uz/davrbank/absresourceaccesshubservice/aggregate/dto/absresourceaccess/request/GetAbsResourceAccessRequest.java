package uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAbsResourceAccessRequest {
    @JsonProperty("iBranch")
    @NotBlank(message = "iBranch is required")
    private String iBranch;

    @JsonProperty("iUserId")
    @JsonAlias("iEmpId")
    @NotNull(message = "iUserId is required")
    @PositiveOrZero(message = "iUserId must be zero or greater")
    private Integer iUserId;

    @JsonProperty("iPageNumber")
    @NotNull(message = "iPageNumber is required")
    @Positive(message = "iPageNumber must be greater than zero")
    private Integer iPageNumber;

    @JsonProperty("iPageSize")
    @NotNull(message = "iPageSize is required")
    @Positive(message = "iPageSize must be greater than zero")
    private Integer iPageSize;
}
