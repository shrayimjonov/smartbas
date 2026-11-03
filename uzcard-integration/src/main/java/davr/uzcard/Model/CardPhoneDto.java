package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPhoneDto {
    @NotBlank(message = "phone field is mandatory")
    @Size(min = 12, max = 16)
    private String phone;

}
