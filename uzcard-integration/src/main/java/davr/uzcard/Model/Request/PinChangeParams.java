package davr.uzcard.Model.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinChangeParams {
    @JsonProperty("cardInfo")
    private PinChangeCardInfo cardInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PinChangeCardInfo {
        @JsonProperty("cardId")
        private String cardId;
        @JsonProperty("merchantId")
        private String merchantId;
        @JsonProperty("terminalId")
        private String terminalId;
        @JsonProperty("ext")
        private String ext;
    }
}
