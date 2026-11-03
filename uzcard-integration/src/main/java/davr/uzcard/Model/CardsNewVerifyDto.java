package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardsNewVerifyDto {
    @NotBlank
    private String id;
    @NotBlank
    private String code;
}
