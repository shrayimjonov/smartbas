package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardsNewVerify {
    @Builder.Default
    private String jsonrpc = "2.0";
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @Builder.Default
    private String method = "cards.new.verify";
    private Params params;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Params {
        private Otp otp;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Otp {
        private String id;
        private String code;
    }


}
