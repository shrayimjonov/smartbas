package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PinResetVerify {
    private String jsonrpc;
    private String method;
    private int id;
    private Params params;

    @Data
    @Builder
    public static class Params{
        private Card card;
    }

    @Data
    @Builder
    public static class Card{
        private String cardId;
        private String merchantId;
        private String terminalId;
        private String port;
        private String ext;
        private String stan;
        private String code;
    }
}
