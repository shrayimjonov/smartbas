package humo.integration.card.Model.Request.v2.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HuMoCardVerifyParams {
    @JsonProperty("pan")
    private String pan;
    @JsonProperty("msisdn")
    private String msisdn;
}
