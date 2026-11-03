package uz.devops.humoMiddleware.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.basic.*;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Клиент Активировать запрос")
public class ClientInfo {

    @Size(max = 256)
    @ApiModelProperty("CardSuite bank identifier, MB_STD by default")
    private String bankId;

    @Size(max = 32)
    @ApiModelProperty("customer sms language")
    private Lang language;

//    @ApiModelProperty("Notification related data")
//    @JsonProperty(value = "Notification")
//    private Object notification;

    @JsonProperty(value = "Charge")
    @ApiModelProperty("Customer charge settings")
    private Charge charge;

    @JsonProperty(value = "Card")
    @ApiModelProperty("Customer card")
    private Card card;

    @JsonProperty(value = "Phone")
    @ApiModelProperty("Customer phone")
    private Phone phone;

    @JsonProperty(value = "Email")
    @ApiModelProperty("Customer email data")
    private Email email;

    @Size(max = 256)
    @ApiModelProperty("Customer access code, could be used for access control to pull services")
    private String accessCode;
}
