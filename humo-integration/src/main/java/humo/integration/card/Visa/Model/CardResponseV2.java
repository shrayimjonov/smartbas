package humo.integration.card.Visa.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponseV2 {

    @JsonProperty("jsonrpc")
    private String jsonrpc;
    @JsonProperty("id")
    private String id;
    @JsonProperty("result")
    private Result result;


    @Data
    @Builder
    public static class Result {
        public String id;
        public String username;
        public String pan;
        public String expiry;
        public int status;
        public String phone;
        public String fullName;
        public Long balance;
        public boolean sms;
        public int pincnt;
        public String cardtype;
        public Long holdAmount;
        public Long cashbackAmount;
        public String aacct;
        public String par;
    }


}
