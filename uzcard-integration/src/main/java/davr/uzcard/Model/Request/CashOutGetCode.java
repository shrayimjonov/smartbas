package davr.uzcard.Model.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class CashOutGetCode {
    @JsonProperty("tran")
    private CashOutTran tran;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    public static class CashOutTran {
        @JsonProperty("amount")
        private Long amount;
        @JsonProperty("cardId")
        private String cardId;
        @JsonProperty("ext")
        private String ext;
    }

}
