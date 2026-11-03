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
@ApiModel(value = "Service")
public class Service {

    @NotNull
    @Size(max = 34)
    @ApiModelProperty("Service package identifier")
    @JsonProperty(value = "serviceID")
    private String serviceId;

    @Size(max = 255)
    @ApiModelProperty("Incoming channel identifier")
    private String serviceChannel;
}
