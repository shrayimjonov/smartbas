package davr.uzcard.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardStatusDto {
    @NotBlank(message = "token field is mandatory")
    private String token;
    @NotBlank(message = "status field is mandatory")
    private int status;
}
