package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenList {
    private String jsonrpc;
    private String method;
    private int id;
    private Params params;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Params {
        private String id;
    }
}
