package davr.uzcard.Model.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BinList {
    @Builder.Default
    private String id = "1";
    @Builder.Default
    private String jsonrpc = "2.0";
    @Builder.Default
    private String method = "get.bin.list";

}
