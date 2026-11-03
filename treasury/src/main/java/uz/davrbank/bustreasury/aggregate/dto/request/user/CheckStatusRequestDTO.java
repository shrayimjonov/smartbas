package uz.davrbank.bustreasury.aggregate.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckStatusRequestDTO {

    @NotBlank(message = "P_TRANS_ID must not be empty")
    @JsonProperty("P_TRANS_ID")
    private String P_TRANS_ID;

    @NotBlank(message = "P_GROUP_ID must not be empty")
    @JsonProperty("P_GROUP_ID")
    private String P_GROUP_ID;
}
