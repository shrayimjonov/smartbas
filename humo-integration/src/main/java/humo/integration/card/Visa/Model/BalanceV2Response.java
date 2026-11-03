package humo.integration.card.Visa.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceV2Response {
    private String status;
    private Boolean isSuccess;
    private Result result;
    private Long timeStamp;
    private Error error;


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
        private Long approvalCode;
        private Long systemsTraceAuditNumber;
        private Long clearedAmount;
        private String clearedCurrencyCode;
        private Long availableAmount;
        private String availableCurrencyCode;
        private Long clearedAfterAmount;
        private String clearedAfterCurrencyCode;
        private Long availableAfterAmount;
        private String availableAfterCurrencyCode;
    }
}
