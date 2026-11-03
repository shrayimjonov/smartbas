package uz.devops.humoMiddleware.dto.request;

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
@ApiModel(value = "запрос удалить карту клиента")
public class PanInfo {

    @NotNull
    @Size(max = 20)
    @ApiModelProperty("primary account number")
    private String pan;
}
