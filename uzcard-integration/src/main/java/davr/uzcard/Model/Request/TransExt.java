package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransExt {
    private String jsonrpc;
    private String method;
    private Params params;
    private String id;

    @Data
    @Builder
    public static class Params{
        public String extId;
    }

}
