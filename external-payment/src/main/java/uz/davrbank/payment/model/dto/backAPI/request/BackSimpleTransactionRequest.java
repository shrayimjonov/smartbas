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
public class BackSimpleTransactionRequest {
    @JsonProperty("BRANCH")
    private String branch;
    @JsonProperty("DOC_NUM")
    private String docNum;
    @JsonProperty("BANK_CL")
    private String bankCl;
    @JsonProperty("ACC_CL")
    private String accCl;
    @JsonProperty("INN_CL")
    private String innCl;
    @JsonProperty("NAME_CL")
    private String nameCl;
    @JsonProperty("BANK_CO")
    private String bankCo;
    @JsonProperty("ACC_CO")
    private String accCo;
    @JsonProperty("INN_CO")
    private String innCo;
    @JsonProperty("NAME_CO")
    private String nameCo;
    @JsonProperty("SUMMA")
    private Long amount;
    @JsonProperty("PURPOSE")
    private String purpose;
    @JsonProperty("CODE_PURPOSE")
    private String purposeCode;
    @JsonProperty("TYPE_DOC")
    private String docType;
    @JsonProperty("PARENT_GROUP_ID")
    private String parentGroupId;
    @JsonProperty("ACTION_ID")
    private String actionId;
    @JsonProperty("CASH_CODE")
    private String cashCode;
    @JsonProperty("CASH_CODE_DESC")
    private String cashCodeDesc;
    @JsonProperty("REQ_ID")
    private String requestId;

    public Map<Object, Object> toMap() {
        var request = new HashMap<>();
        request.put("BRANCH", this.branch);
        request.put("DOC_NUM", this.docNum);
        request.put("BANK_CL", this.bankCl);
        request.put("ACC_CL", this.accCl);
        request.put("INN_CL", this.innCl);
        request.put("NAME_CL", this.nameCl);
        request.put("BANK_CO", this.bankCo);
        request.put("ACC_CO", this.accCo);
        request.put("INN_CO", this.innCo);
        request.put("NAME_CO", this.nameCo);
        request.put("SUMMA", this.amount);
        request.put("PURPOSE", this.purpose);
        request.put("CODE_PURPOSE", this.purposeCode);
        request.put("TYPE_DOC", this.docType);
        request.put("PARENT_GROUP_ID", this.parentGroupId);
        request.put("ACTION_ID", this.actionId);
        request.put("CASH_CODE", this.cashCode);
        request.put("CASH_CODE_DESC", this.cashCodeDesc);
        request.put("REQ_ID", this.requestId);
        return request;
    }
}
