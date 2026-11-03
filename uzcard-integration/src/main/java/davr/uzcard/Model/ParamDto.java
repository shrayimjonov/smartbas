package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamDto {
    @NotBlank
    private String name;
    @NotBlank
    private String value;
}
