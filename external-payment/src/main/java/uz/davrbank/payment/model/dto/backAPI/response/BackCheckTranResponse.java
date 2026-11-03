package uz.davrbank.payment.model.dto.backAPI.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackCheckTranResponse {
    @JsonProperty("field_name")
    private String key;
    @JsonProperty("field_value")
    private String value;

}
