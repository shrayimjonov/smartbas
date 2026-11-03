package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardsNew {

    private String jsonrpc;
    private String method;
    private int id;
    private Params params;

    @Data
    @Builder
    public static class Params {
        private Card card;

        @Data
        @Builder
        public static class Card {
            private String pan;
            private String expiry;
        }
    }


}
