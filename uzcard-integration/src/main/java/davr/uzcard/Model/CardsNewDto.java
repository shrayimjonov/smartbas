package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardsNewDto {
    @NotBlank(message = "card Number field is mandatory")
    @Size(min = 16, max = 16)
    private String cardPan;
    @NotBlank(message = "card expiry Date field is mandatory")
    @Size(min = 4)
    private String expiry;
    private String phone;
}
