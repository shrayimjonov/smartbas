package humo.integration.card.Model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerListReq {
    @JsonProperty("id")
    @ApiModelProperty("You can fill like this: 123456789")
    private String id = "123456789";
    @JsonProperty("params")
    @NonNull
    @ApiModelProperty("Required fields: phone number and bank Id")
    private CustomerListParam params;

}
