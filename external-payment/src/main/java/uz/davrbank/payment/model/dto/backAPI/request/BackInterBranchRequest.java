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
public class BackInterBranchRequest {
    @JsonProperty("BRANCH")
    private String branch;
    @JsonProperty("BANK_CL")
    private String bankCl;
    @JsonProperty("ACC_CL")
    private String accCl;
    @JsonProperty("NAME_CL")
    private String nameCl;
    @JsonProperty("BANK_CO")
    private String bankCo;
    @JsonProperty("ACC_CO")
    private String accCo;
    @JsonProperty("NAME_CO")
    private String nameCo;
    @JsonProperty("DOC_NUM")
    private String docNum;
    @JsonProperty("CURRENCY")
    private String currency;
    @JsonProperty("SUMMA")
    private Long amount;
    @JsonProperty("PURPOSE")
    private String purpose;
    @JsonProperty("ID_TRANSH")
    private String idTransh;
    @JsonProperty("TYPE_DOC")
    private String typeDoc;
    @JsonProperty("PARENT_GROUP_ID")
    private String parentGroupId;
    @JsonProperty("REQ_ID")
    private String requestId;

    public Map<Object, Object> toMap() {
        var request = new HashMap<>();
        request.put("BRANCH", this.branch);
        request.put("BANK_CL", this.bankCl);
        request.put("ACC_CL", this.accCl);
        request.put("NAME_CL", this.nameCl);
        request.put("BANK_CO", this.bankCo);
        request.put("ACC_CO", this.accCo);
        request.put("NAME_CO", this.nameCo);
        request.put("DOC_NUM", this.docNum);
        request.put("CURRENCY", this.currency);
        request.put("SUMMA", this.amount);
        request.put("PURPOSE", this.purpose);
        request.put("ID_TRANSH", this.idTransh);
        request.put("TYPE_DOC", this.typeDoc);
        request.put("PARENT_GROUP_ID", this.parentGroupId);
        request.put("REQ_ID", this.requestId);
        return request;
    }
}
