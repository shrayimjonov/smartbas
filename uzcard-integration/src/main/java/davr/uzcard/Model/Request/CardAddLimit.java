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
public class CardAddLimit {
    @Builder.Default
    private String jsonrpc = "2.0";
    @Builder.Default
    private String method ="card.limit.add";
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private Params params;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Params{
        private LimitInfo limitInfo;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LimitInfo{
        private String cardId;
        private ArrayList<Limit> limits;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Limit{
        private Integer limitId;
        private Long amount;
    }
}
