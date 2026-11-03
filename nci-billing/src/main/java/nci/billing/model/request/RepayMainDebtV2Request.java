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
public class RepayMainDebtV2Request {
    @JsonProperty("BRANCH")
    public String branch;
    @JsonProperty("ID")
    public String id;
    @JsonProperty("SUMMA")
    public String amount;
    @JsonProperty("BY_MAIN_ACCOUNT")
    public String byMainAccount;
}
