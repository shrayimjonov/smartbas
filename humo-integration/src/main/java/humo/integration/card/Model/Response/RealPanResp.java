package humo.integration.card.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealPanResp {

    private String id;
    private Result result;

    @Data
    public static class Result {
        private String pan;
        private String bank_c;
        private String group_c;

    }

}
