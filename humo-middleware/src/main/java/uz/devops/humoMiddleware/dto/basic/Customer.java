package uz.devops.humoMiddleware.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Customer")
public class Customer {
    @NotNull
    @Size(max = 256)
    @ApiModelProperty("Customer identifier.")
    private String customerId;

    @Size(max = 256)
    @ApiModelProperty("CardSuite bank identifier")
    private String bankId;

    @JsonProperty(value = "Card")
    private List<Card> card;
}
