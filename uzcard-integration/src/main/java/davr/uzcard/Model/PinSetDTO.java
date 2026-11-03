package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinSetDTO {
    @NotBlank
    private String token;
    private Integer preset = 0;

}
