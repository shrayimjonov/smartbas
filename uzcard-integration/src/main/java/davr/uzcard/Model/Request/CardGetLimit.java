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
public class CardGetLimit {
    @Builder.Default
    private String jsonrpc ="2.0";
    @Builder.Default
    private String method = "card.limit.info";
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private Params params;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Params{
        private String cardId;
    }
}
