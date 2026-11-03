package humo.integration.card.Model.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IacsCardV3Resp {
    @JsonProperty("id")
    private String id;
    @JsonProperty("result")
    private Result result;
    @JsonProperty("error")
    private IIACSError error;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IIACSError {
        private Integer code;
        private String message;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Result {
        @JsonProperty("listSize")
        private int listSize;
        @JsonProperty("card")
        private Card card;
        @JsonProperty("account")
        private ArrayList<Account> account;
        @JsonProperty("balance")
        private Balance balance;
        @JsonProperty("mb")
        private Mb mb;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Card {
        @JsonProperty("institutionId")
        private String institutionId;
        @JsonProperty("primaryAccountNumber")
        private String primaryAccountNumber;
        @JsonProperty("effectiveDate")
        private Date effectiveDate;
        @JsonProperty("updateDate")
        private Date updateDate;
        @JsonProperty("prefixNumber")
        private int prefixNumber;
        @JsonProperty("expiry")
        private String expiry;
        @JsonProperty("cardSequenceNumber")
        private String cardSequenceNumber;
        @JsonProperty("cardholderId")
        private String cardholderId;
        @JsonProperty("nameOnCard")
        private String nameOnCard;
        @JsonProperty("accountRestrictionsFlag")
        private String accountRestrictionsFlag;
        @JsonProperty("commissionGroup")
        private String commissionGroup;
        @JsonProperty("cardUserId")
        private String cardUserId;
        @JsonProperty("additionalInfo")
        private String additionalInfo;
        @JsonProperty("riskGroup")
        private String riskGroup;
        @JsonProperty("riskGroup2")
        private String riskGroup2;
        @JsonProperty("bankC")
        private String bankC;
        @JsonProperty("pinTryCount")
        private String pinTryCount;
        @JsonProperty("statuses")
        private Statuses statuses;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Balance {
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("initialAmount")
        private String initialAmount;
        @JsonProperty("bonusAmount")
        private String bonusAmount;
        @JsonProperty("creditLimit")
        private String creditLimit;
        @JsonProperty("lockedBackofficeAmount")
        private String lockedBackOfficeAmount;
        @JsonProperty("lockedBackofficeAmountOffline")
        private String lockedBackOfficeAmountOffline;
        @JsonProperty("lockedAmount")
        private String lockedAmount;
        @JsonProperty("availableAmount")
        private String availableAmount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Account {
        @JsonProperty("institutionId")
        private String institutionId;
        @JsonProperty("accountId")
        private String accountId;
        @JsonProperty("bankAccountId")
        private String bankAccountId;
        @JsonProperty("currency")
        private String currency;
        @JsonProperty("cardholderId")
        private String cardholderId;
        @JsonProperty("effectiveDate")
        private Date effectiveDate;
        @JsonProperty("updateDate")
        private Date updateDate;
        @JsonProperty("accountType")
        private String accountType;
        @JsonProperty("initialAmount")
        private String initialAmount;
        @JsonProperty("bonusAmountExpiry")
        private Date bonusAmountExpiry;
        @JsonProperty("lockedBackofficeAmount")
        private String lockedBackOfficeAmount;
        @JsonProperty("lockTime")
        private Date lockTime;
        @JsonProperty("shadowAmount")
        private String shadowAmount;
        @JsonProperty("commissionGroup")
        private String commissionGroup;
        @JsonProperty("priority")
        private String priority;
        @JsonProperty("status")
        private String status;
        @JsonProperty("availableAmount")
        private String availableAmount;
        private String transitAccount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Mb {
        @JsonProperty("state")
        private String state;
        @JsonProperty("phone")
        private String phone;
        @JsonProperty("message")
        private String message;
        @JsonProperty("error")
        private Error error;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        private int code;
        private String message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Statuses {
        @JsonProperty("item")
        private ArrayList<Item> item;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {
        @JsonProperty("type")
        private String type;
        @JsonProperty("actionCode")
        private String actionCode;
        @JsonProperty("actionDescription")
        private String actionDescription;
        @JsonProperty("effectiveDate")
        private Date effectiveDate;
    }


}
