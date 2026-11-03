package humo.integration.card.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaskedPanReq {
    private String id = "12345";
    private Params params;

    @Data
    @Builder
    public static class Params {
        private String pan;
    }

}
