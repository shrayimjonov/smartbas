package uz.davrbank.payment.model.dto.backAPI.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
public class BackForexResponse extends BackBaseResponse {
    @JsonProperty("id1")
    private String id1;
    @JsonProperty("id2")
    private String id2;
}
