package humo.integration.card.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckCardToPinflRequestDTO {
    @NotBlank
    @Pattern(regexp = "\\d{16}")
    private String card;
    @NotBlank
    @Pattern(regexp = "\\d{14}")
    private String personCode;
}
