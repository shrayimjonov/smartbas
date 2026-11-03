package humo.integration.card.Model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HumoTransitAccountResp {
    @JsonProperty("tranz_ACCT")
    private String transitAccount;
    private int code;
    private String error;
}
