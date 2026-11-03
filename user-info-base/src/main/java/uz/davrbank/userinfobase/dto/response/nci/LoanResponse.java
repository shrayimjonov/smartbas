package uz.davrbank.userinfobase.dto.response.nci;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoanResponse(

        @JsonProperty("successCode")
        Integer successCode,

        @JsonProperty("isSuccess")
        Boolean isSuccess,

        @JsonProperty("status")
        String status,

        @JsonProperty("result")
        Result result,

        @JsonProperty("timeStamp")
        Long timeStamp

) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Result(

            @JsonProperty("mfo")
            String mfo,

            @JsonProperty("client_name")
            String clientName,

            @JsonProperty("client_birthday")
            LocalDate clientBirthday,

            @JsonProperty("main_acc_balance")
            Long mainAccBalance,

            @JsonProperty("transit_balance")
            Long transitBalance,

            @JsonProperty("principal_balance")
            Long principalBalance,

            @JsonProperty("arrears_on_principal")
            Long arrearsOnPrincipal,

            @JsonProperty("of_overdue_debt")
            Long ofOverdueDebt,

            @JsonProperty("interest_on_term_debt")
            Long interestOnTermDebt,

            @JsonProperty("of_overdue_interest")
            Long ofOverdueInterest,

            @JsonProperty("arrears_of_interest")
            Long arrearsOfInterest,

            @JsonProperty("overdue_covenant_commission")
            Long overdueCovenantCommission,

            @JsonProperty("recommended_amount")
            Long recommendedAmount,

            @JsonProperty("max_amount")
            Long maxAmount,

            @JsonProperty("loan_amount")
            Long loanAmount,

            @JsonProperty("loan_close")
            LocalDate loanClose,

            @JsonProperty("loan_open")
            LocalDate loanOpen,

            @JsonProperty("transit_state")
            Long transitState,

            @JsonProperty("transit_account")
            String transitAccount,

            @JsonProperty("loan_type")
            String loanType,

            @JsonProperty("loss_reserve")
            Long lossReserve,

            @JsonProperty("currency")
            String currency,

            @JsonProperty("court_costs")
            Long courtCosts

    ) {
    }
}