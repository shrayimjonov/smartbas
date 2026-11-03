package humo.integration.card.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HumoVirtualResp {
    private int status;
    private Object message;
    private Result result;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        private String branch;
        private String account;
        private String bankClientId;
        private String humoClientId;
        private String expiry;
        private String cardNumber;
        private String phone;
        private String cardholderName;
        private String cardType;
    }
}
