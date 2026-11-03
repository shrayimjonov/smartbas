package uz.davrbank.userinfobase.dto.response.nci;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.List;

public record LoanByPinflResponse(

        @JsonProperty("successCode")
        Integer successCode,

        @JsonProperty("isSuccess")
        Boolean isSuccess,

        @JsonProperty("status")
        String status,

        @JsonProperty("result")
        List<Result> result,

        @JsonProperty("timeStamp")
        Long timeStamp

) {

    public record Result(

            @JsonProperty("id")
            String id,

            @JsonProperty("amount")
            String amount,

            @JsonProperty("eq_NUM")
            String eqNum,

            @JsonProperty("branch")
            String branch,

            @JsonProperty("state")
            String state,

            @JsonProperty("client")
            String client,

            @JsonProperty("d_DATE")
            String dDate,

            @JsonProperty("currency")
            String currency,

            @JsonProperty("target")
            String target

    ) {}
}
