package uz.devops.humoMiddleware.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Phone")
public class Phone {

    @NotNull
    @Size(max = 256)
    @ApiModelProperty(value = "Phone number, using international format",example = "+998901234567")
    @JsonProperty(value = "msisdn")
    private String phoneNumber;

    @Size(max = 256)
    @ApiModelProperty("kannel gateway SMSC identifier for push service sms messages. Default value")
    private String deliveryChannel;
}
