package humo.integration.card.Model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IacsCardV3Req {
    @JsonProperty("id")
    private String id = "1k23j25hlgf2hhf5";
    @JsonProperty("params")
    private IacsParams params;

    @Data
    @Builder
    public static class IacsParams {
        @JsonProperty("primaryAccountNumber")
        private String primaryAccountNumber;
        @JsonProperty("mb_flag")
        private int mb_flag;
    }
}
