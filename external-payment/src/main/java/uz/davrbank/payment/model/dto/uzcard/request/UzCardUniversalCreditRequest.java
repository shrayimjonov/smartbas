package uz.davrbank.payment.model.dto.uzcard.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UzCardUniversalCreditRequest {
    @JsonProperty("credit")
    private UniversalCredit credit;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UniversalCredit {
        @JsonProperty("ext")
        private String ext;
        @JsonProperty("amount")
        private Long amount;
        @JsonProperty("merchantId")
        private String merchantId;
        @JsonProperty("terminalId")
        private String terminalId;
        @JsonProperty("recipient")
        private String recipient;
        @JsonProperty("sender")
        private UniversalCreditSender sender;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UniversalCreditSender {
        @JsonProperty("system")
        private String system;
        @JsonProperty("legalName")
        private String legalName;
        @JsonProperty("lastName")
        private String lastName;
        @JsonProperty("firstName")
        private String firstName;
        @JsonProperty("middleName")
        private String middleName;
        @JsonProperty("id")
        private String id;
        @JsonProperty("refNum")
        private String refNum;
        @JsonProperty("doc")
        private UniversalCreditDoc doc;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UniversalCreditDoc {
        @JsonProperty("nationality")
        private String nationality;
        @JsonProperty("type")
        private String type;
        @JsonProperty("seriesNumber")
        private String seriesNumber;
        @JsonProperty("birthDate")
        private String birthDate; //dd.MM.yyyy
        @JsonProperty("validTo")
        private String validTo; //dd.MM.yyyy
        @JsonProperty("mrz")
        private String personCode;
    }
}
