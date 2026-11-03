package davr.uzcard.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardVerifyDto {
    @NotBlank(message = "token is mandatory field")
    private String token;
    @NotBlank(message = "code is mandatory field")
    private String code;

}
