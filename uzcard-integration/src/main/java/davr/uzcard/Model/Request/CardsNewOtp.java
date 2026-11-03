package davr.uzcard.Model.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardsNewOtp {
    @Builder.Default
    private String jsonrpc = "2.0";
    @Builder.Default
    private String method = "cards.new.otp";
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private Params params;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Params {
        private Card card;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Card {
        private String pan;
        private String expiry;
        private Sms sms;
        private String requestorPhone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Sms {
        private String ussd;
        private String hash;
        private int templateId;
        private String serviceName;
    }

}
