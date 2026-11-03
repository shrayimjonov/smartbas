package humo.integration.card.Visa.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountListV2Response {
    private String status;
    private Boolean isSuccess;
    private Result result;
    private Long timeStamp;
    private BalanceV2Response.Error error;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        private Long code;
        private String message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private Account account;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account{
        public String institutionId;
        public String accountId;
        public String bankAccountId;
        public String cardholderId;
        public String effectiveDate;
        public String updateDate;
        public String accountType;
        public Long lockedBackofficeAmount;
        public String lockTime;
        public Long shadowAmount;
        public String commissionGroup;
        public String priority;
        public String status;
        public Long availableAmount;
    }
}
