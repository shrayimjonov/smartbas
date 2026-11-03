package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitRemoveDto {
    @NotBlank
    private String token;
    private ArrayList<Integer> limitCodes;
}
