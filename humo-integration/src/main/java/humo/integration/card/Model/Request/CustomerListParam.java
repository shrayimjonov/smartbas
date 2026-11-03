package humo.integration.card.Model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerListParam {
    @JsonProperty("phone")
    @ApiModelProperty(value = "Card Holder Phone number")
    private String phone;
    @JsonProperty("bankId")
    @ApiModelProperty(value = "BankId always = MB_STD ")
    private String bankId = "MB_STD";
}
