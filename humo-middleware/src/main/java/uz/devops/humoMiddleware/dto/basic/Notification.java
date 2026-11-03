package uz.devops.humoMiddleware.dto.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Notification")
public class Notification {

    @Size(max = 9)
    @ApiModelProperty("Time zone (HH – hours, MM – minutes)")
    private String timeZone;

    @Size(max = 11)
    @ApiModelProperty("Time interval for receiving notifications")
    private String timeInternal;

    @Size(max = 256)
    @ApiModelProperty("Phone number or alphanumeric string, could be used with accept from SMSC/SMS service provider")
    private String sender;
}
