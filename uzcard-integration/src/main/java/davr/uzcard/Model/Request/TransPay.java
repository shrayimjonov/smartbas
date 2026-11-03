package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransPay {
    private int id;
    private String jsonrpc;
    private String method;
    private Params params;

    @Data
    @Builder
    public static class Params {
        private Tran tran;
    }

    @Data
    @Builder
    public static class Tran {
        private long amount;
        private String cardId;
        private String ext;
        private String merchantId;
        private int port;
        private String stan;
        private String terminalId;
    }

}
