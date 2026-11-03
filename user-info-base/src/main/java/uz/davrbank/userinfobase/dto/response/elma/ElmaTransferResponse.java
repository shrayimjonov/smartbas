package uz.davrbank.userinfobase.dto.response.elma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElmaTransferResponse {
    private String status;  // This will capture the "status" field in the response
    private DataContent data;  // This will capture the "data" field in the response

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DataContent {
        private String action;
        private String method;
        private String server;
        private MessageContent messageContent;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class MessageContent {
            @JsonProperty("LoanId")
            private String loanId;  // Matches the LoanId field in the response
            @JsonProperty("branchId")
            private String branchId;  // Matches the branchId field in the response
        }
    }
}