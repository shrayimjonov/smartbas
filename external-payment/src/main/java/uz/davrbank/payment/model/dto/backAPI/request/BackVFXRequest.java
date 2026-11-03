package uz.davrbank.payment.model.dto.backAPI.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BackVFXRequest {
    @JsonProperty("BRANCH")
    private String branch;
    @JsonProperty("CARD_ACC")
    private String accountDebit;
    @JsonProperty("ACC_TRANSIT_VAL")
    private String accountCredit;
    @JsonProperty("COURSE")
    private Long course;
    @JsonProperty("SUMMA")
    private Long amount;
    @JsonProperty("PURPOSE")
    private String purpose;
    @JsonProperty("REQ_ID")
    private String requestId;
}
