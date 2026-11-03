package humo.integration.card.Model.Response.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import humo.integration.card.Model.Dto.core.response.CoreError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HuMoCardVerifyBaseResponse<R> {
    @JsonProperty("id")
    private String id;
    @JsonProperty("result")
    private R result;

    @JsonProperty("error")
    private CoreError error;




}
