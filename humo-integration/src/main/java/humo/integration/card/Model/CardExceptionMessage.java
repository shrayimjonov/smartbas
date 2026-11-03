package humo.integration.card.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardExceptionMessage {
    private String id;
    private Error error;

    @Data
    public static class Error {
        private String code;
        private String message;
    }

}
