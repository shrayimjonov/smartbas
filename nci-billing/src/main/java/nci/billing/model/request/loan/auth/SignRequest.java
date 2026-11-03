package nci.billing.model.request.loan.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignRequest {
    @JsonProperty("grant_type")
    private String grantType;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
