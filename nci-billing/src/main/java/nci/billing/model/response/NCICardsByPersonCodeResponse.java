package nci.billing.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NCICardsByPersonCodeResponse {
    @JsonProperty("branch")
    private String branch;
    @JsonProperty("expiry")
    private String expiry;
    @JsonProperty("mc_NAME")
    private String holder;
    @JsonProperty("client")
    private String client;
    @JsonProperty("card_NUMBER")
    private String pan;
}
