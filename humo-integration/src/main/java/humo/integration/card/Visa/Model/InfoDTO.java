package humo.integration.card.Visa.Model;

import humo.integration.card.Visa.Model.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoDTO {
    @NotBlank
    private String card;
    @NotBlank
    @Pattern(regexp = "\\d{4}")
    private String expiry;
    private CardType type;


}
