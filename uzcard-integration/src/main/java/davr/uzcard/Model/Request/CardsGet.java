package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardsGet {
    private String jsonrpc;
    private String method;
    private int id;
    private Params params;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Params{
        private ArrayList<String> ids;
    }

}
