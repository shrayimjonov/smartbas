package nci.billing.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanResponse {
    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("mfo")
    private String mfo;
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("client_birthday")
    private String birthday;
    @JsonProperty("main_acc_balance")
    private Long mainAccountBalance;
    @JsonProperty("principal_balance")
    private Long principalBalance;
    @JsonProperty("arrears_on_principal")
    private Long arrearsOnPrincipal;
    @JsonProperty("of_overdue_debt")
    private Long ofOverdueDebt;
    @JsonProperty("interest_on_term_debt")
    private Long interestOnTermDebt;
    @JsonProperty("of_overdue_interest")
    private Long ofOverdueInterest;
    @JsonProperty("arrears_of_interest")
    private Long arrearsOfInterest;
    @JsonProperty("recommended_amount")
    private Long recommendedAmount;
    @JsonProperty("max_amount")
    private Long maxAmount;
    @JsonProperty("transit_balance")
    private Long transitBalance;
    @JsonProperty("overdue_covenant_commission")
    private Long overdueCovenantCommission;
    @JsonProperty("loan_amount")
    private Long loanAmount;
    @JsonProperty("loan_close")
    private String loanClose;
    @JsonProperty("loan_open")
    private String loanOpen;
    @JsonProperty("error")
    private Error error;
    @JsonProperty("loan_type")
    private String loanType;
    @JsonProperty("loss_reserve")
    private Long lossReserve;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("court_costs")
    private Long court_costs;
    @JsonProperty("transit_account")
    private String transitAccount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        private Integer code;
        private String message;
    }

}
