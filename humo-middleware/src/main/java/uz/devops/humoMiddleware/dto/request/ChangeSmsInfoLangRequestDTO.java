package uz.devops.humoMiddleware.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.basic.Lang;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "запрос на изменение языка сообщений держателя карты")
public class ChangeSmsInfoLangRequestDTO {

    @NotNull
    @Size(max = 256)
    @ApiModelProperty("Customer identifier")
    private String customerId;

    @NotNull
    @Size(max = 32)
    @ApiModelProperty("Customer sms language")
    private Lang language;
}
