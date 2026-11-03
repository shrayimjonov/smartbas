package humo.integration.card.Visa.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardV2Request {
    @NotBlank
    private String card;
    @Builder.Default
    private String subSystem = "VISA_MOBILE_BACK";

}
