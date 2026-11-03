package humo.integration.card.Model.Request.v2.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import humo.integration.card.Model.CheckCardToPinflRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuMoCardCheckToPinflParams {
    @JsonProperty("person_code")
    private String person_code;
    @JsonProperty("pan")
    private String pan;

    public HuMoCardCheckToPinflParams(CheckCardToPinflRequestDTO request) {
        this.person_code = request.getPersonCode();
        this.pan = request.getCard();
    }
}
