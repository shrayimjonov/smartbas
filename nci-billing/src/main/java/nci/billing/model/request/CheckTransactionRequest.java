package nci.billing.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckTransactionRequest {
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("login")
    private String login;
    @JsonProperty("pass")
    private String password;
}
