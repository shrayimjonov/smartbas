package nci.billing.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DebtsListResponse {
    @JsonProperty("result")
    private String result;
    @JsonProperty("resultnote")
    private String resultNote;
    @JsonProperty("inf")
    private ArrayList<Info> info;
    @JsonProperty("error")
    private LoanResponse.Error error;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Info {
        @JsonProperty("recommended_amount")
        private String recommendedAmount;
        @JsonProperty("max_amount")
        private String maxAmount;
        @JsonProperty("branch")
        private String branch;
        @JsonProperty("id")
        private String loanId;
    }

}

