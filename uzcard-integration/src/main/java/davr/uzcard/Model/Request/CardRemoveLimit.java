package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardRemoveLimit {
    @Builder.Default
    private String jsonrpc = "2.0";
    @Builder.Default
    private String method = "card.limit.remove";
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private Params params;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LimitInfo {
        private String cardId;
        private ArrayList<Integer> limits;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Params {
        private LimitInfo limitInfo;
    }
}
