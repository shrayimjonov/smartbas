package uz.davrbank.payment.model.dto.absHub.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ABSTransactionSimpleResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("branch")
    private String branch;
    @JsonProperty("result")
    private Integer result;
    @JsonProperty("info")
    private String info;
}
