package humo.integration.card.Model.Dto.core.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardVerifyRequestDTO {
    @NotBlank
    @Pattern(regexp = "\\d{16}")
    private String pan;
    @NotBlank
    @Pattern(regexp = "^\\+\\d{12}$")
    private String phone;
}
