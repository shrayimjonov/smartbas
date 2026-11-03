package humo.integration.card.Model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListResp {
    @JsonProperty("id")
    public String id;
    @JsonProperty("result")
    public Result result;

    @Data
    public static class Result {
        @JsonProperty("Customer")
        public ArrayList<Customer> customer;
    }

    @Data
    public static class Customer {
        @JsonProperty("customerId")
        public String customerId;
        @JsonProperty("bankId")
        public String bankId;
        @JsonProperty("cardholderName")
        public String cardholderName;
        @JsonProperty("Card")
        public ArrayList<Card> card;
    }

    @Data
    public static class Card {
        @JsonProperty("state")
        public String state;
        @JsonProperty("pan")
        public String pan;
        @JsonProperty("expiry")
        public String expiry;
        @JsonProperty("Service")
        public ArrayList<Service> service;
        @JsonProperty("label")
        public String label;
        @JsonProperty("Charge")
        public Charge charge;
    }

    @Data
    public static class Charge {
        @JsonProperty("agreementCharge")
        public String agreementCharge;
        @JsonProperty("chargeAccount")
        public String chargeAccount;
    }

    @Data
    public static class Service {
        @JsonProperty("serviceID")
        public String serviceID;
        @JsonProperty("serviceChannel")
        public String serviceChannel;
    }


}
