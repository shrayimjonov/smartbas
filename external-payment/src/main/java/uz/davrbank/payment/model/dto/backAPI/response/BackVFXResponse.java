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
public class BackVFXResponse extends BackBaseResponse {
    @JsonProperty("id_sum")
    private String idSum;
    @JsonProperty("id_val")
    private String idVal;
    @JsonProperty("id_dox")
    private String idDox;
}
