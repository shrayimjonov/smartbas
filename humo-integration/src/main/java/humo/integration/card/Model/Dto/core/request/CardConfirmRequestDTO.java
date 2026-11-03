package humo.integration.card.Model.Dto.core.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardConfirmRequestDTO {
    @NotBlank
    @Pattern(regexp = "\\d{16}")
    private String pan;
    @NotBlank
    private String code;
}
