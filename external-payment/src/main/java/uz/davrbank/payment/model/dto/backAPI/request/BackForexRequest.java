package uz.davrbank.payment.model.dto.backAPI.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BackForexRequest {
    @JsonProperty("BRANCH")
    public String branch;
    @JsonProperty("ID_FX")
    public String idFx;
    @JsonProperty("ACC_CL")
    public String accCl;
    @JsonProperty("ACC_CO")
    public String accCo;
    @JsonProperty("CURRENCY")
    public String currency;
    @JsonProperty("SUMMA")
    public Long amount;
    @JsonProperty("PURPOSE")
    public String purpose;
    @JsonProperty("ID_TRANSH")
    public String idTransh;
    @JsonProperty("REQ_ID")
    private String requestId;

    public Map<Object, Object> toMap() {
        var request = new HashMap<>();
        request.put("BRANCH", this.branch);
        request.put("ID_FX", this.idFx);
        request.put("ACC_CL", this.accCl);
        request.put("ACC_CO", this.accCo);
        request.put("CURRENCY", this.currency);
        request.put("SUMMA", this.amount);
        request.put("purpose", this.purpose);
        request.put("ID_TRANSH", this.idTransh);
        request.put("REQ_ID", this.requestId);
        return request;
    }
}
