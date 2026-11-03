package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashOutDTO {
    @NotBlank
    private String token;
    @Min(1)
    private Long amount = 0L;
    @NotBlank
    private String extId;


}
