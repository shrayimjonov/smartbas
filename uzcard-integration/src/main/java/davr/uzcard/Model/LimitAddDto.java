package davr.uzcard.Model;

import davr.uzcard.Model.Request.CardAddLimit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitAddDto {
    @NotBlank
    private String token;
    private ArrayList<CardAddLimit.Limit> limits;
}
