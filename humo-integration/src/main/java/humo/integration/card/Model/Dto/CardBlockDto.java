package humo.integration.card.Model.Dto;

import humo.integration.card.Model.enums.BlockType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardBlockDto {
    private BlockType type;
    @NotBlank
    @Pattern(regexp = "\\d{16}")
    private String card;
    @NotBlank
    private String cause;
}
