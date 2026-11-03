package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckPinflParams {
    private Cardinfo cardinfo;

    public CheckPinflParams(String cardId, String pinfl) {
        this.cardinfo = new Cardinfo(cardId, pinfl);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cardinfo {
        private String cardId;
        private String pinfl;
    }
}
