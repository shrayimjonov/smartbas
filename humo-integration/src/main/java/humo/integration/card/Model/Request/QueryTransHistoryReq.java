package humo.integration.card.Model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryTransHistoryReq implements Serializable {
    @NotBlank(message = "Card pan field is mandatory")
    @ApiModelProperty(value = "Card pan number consist 16 digits")
    @JsonProperty("pan")
    private String pan;
    @NotBlank(message = "Begin Date field is mandatory")
    @ApiModelProperty(value = "Transaction history begin date")
    @JsonProperty("beginDate")
    public String beginDate;
    @NotBlank(message = "Finish Date field is mandatory")
    @ApiModelProperty(value = "Transaction history end date")
    @JsonProperty("finishDate")
    public String finishDate;
}
