package humo.integration.card.Visa.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardV2Response {
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
        private Card card;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Card {
        private String institutionId;
        private String primaryAccountNumber;
        private String effectiveDate;
        private String updateDate;
        private String prefixNumber;
        private String expiry;
        private String cardSequenceNumber;
        private String cardholderId;
        private String nameOnCard;
        private String cardholderPassword;
        private String accountRestrictionsFlag;
        private String commissionGroup;
        private String cardUserId;
        private String additionalInfo;
        private String riskGroup;
        private String riskGroup2;
        private Statuses statuses;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Statuses{
        private ArrayList<Item> item;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String type;
        private String actionCode;
        private String actionDescription;
        private String effectiveDate;
    }

}
