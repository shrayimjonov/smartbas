package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardTokenListDto {
    @NotBlank(message = "card Token list field is mandatory")
    private ArrayList<String> token;

}
