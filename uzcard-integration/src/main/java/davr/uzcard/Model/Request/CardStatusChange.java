package davr.uzcard.Model.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardStatusChange {
    private int id;
    private String jsonrpc;
    private String method;
    private Params params;

    @Data
    @Builder
    public static class Params {
        private Card card;
    }

    @Data
    @Builder
    public static class Card {
        public String cardId;
        public String terminalId;
        public String merchantId;
        public String port;
        public String stan;
        public String ext;
        public int statusId;
    }

}
