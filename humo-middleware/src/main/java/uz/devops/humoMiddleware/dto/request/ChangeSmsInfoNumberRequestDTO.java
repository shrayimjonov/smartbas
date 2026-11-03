package uz.devops.humoMiddleware.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.basic.Phone;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "запрос на изменение номера телефона")
public class ChangeSmsInfoNumberRequestDTO {

    @NotNull
    @Size(max = 256)
    @ApiModelProperty("Customer identifier")
    private String customerId;

    @JsonProperty(value="Phone")
    @ApiModelProperty("Customer phone")
    private Phone phone;
}
