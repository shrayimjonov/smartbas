package uz.davrbank.payment.model.dto.absHub.request;

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
public class ABSTransactionSimpleRequest {
    @JsonProperty("branch")
    private String branch;
    @JsonProperty("doc_num")
    private String docNum;
    @JsonProperty("branch_debit")
    private String branchDebit;
    @JsonProperty("account_debit")
    private String accountDebit;
    @JsonProperty("branch_credit")
    private String branchCredit;
    @JsonProperty("account_credit")
    private String accountCredit;
    @JsonProperty("purpose")
    private String purpose;
    @JsonProperty("purpose_code")
    private String purposeCode;
    @JsonProperty("doc_type")
    private String docType;
    @JsonProperty("parentId")
    private String parentId;
    @JsonProperty("action_id")
    private String actionId;
    @JsonProperty("amount")
    private Long amount;
    @JsonProperty("request_id")
    private String requestId;

    public Map<Object, Object> toMap(){
        Map<Object, Object> map = new HashMap<>();
        map.put("branch", branch);
        map.put("doc_num", docNum);
        map.put("branch_debit", branchDebit);
        map.put("account_debit", accountDebit);
        map.put("branch_credit", branchCredit);
        map.put("account_credit", accountCredit);
        map.put("purpose", purpose);
        map.put("purpose_code", purposeCode);
        map.put("doc_type", docType);
        map.put("parentId", parentId);
        map.put("action_id", actionId);
        map.put("amount", amount);
        map.put("request_id", requestId);
        return map;
    }
}
