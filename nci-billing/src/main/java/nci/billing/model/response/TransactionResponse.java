package nci.billing.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    public String id;
    public String branch;
    @JsonProperty("acc_CL")
    public String clientAccount;
    @JsonProperty("name_CL")
    public String clientName;
    @JsonProperty("acc_CO")
    public String correspondenceAccount;
    @JsonProperty("name_CO")
    public String correspondenceName;
    public String purpose;
    public String summa;
    @JsonProperty("v_DATE")
    public String date;
    public String status;
}
