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
public class BalanceGet {

    private String jsonrpc;
    private int id;
    private String method;
    private Params params;

    @Data
    @Builder
    public static class Params{
        private ArrayList<String> ids;
    }

}
