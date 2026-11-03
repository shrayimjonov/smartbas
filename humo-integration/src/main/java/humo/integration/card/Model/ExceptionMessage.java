package humo.integration.card.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {
    private String id;
    @JsonProperty("error")
    private Error error;

    public ExceptionMessage(int i, String message) {
        this.id = String.valueOf(i);
        this.error = new Error(String.valueOf(i), message);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Error {
        private String code;
        private String message;
    }
}
