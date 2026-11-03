package uz.devops.humoMiddleware.dto.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "Mobile Bank")
public class MobileBank {

    @NotNull
    @ApiModelProperty("State of the mobile banking")
    private String state;

    @ApiModelProperty("Phone number, using international format.")
    private String phone;

    @NotNull
    @ApiModelProperty("Description of the mb state")
    private String message;
}
