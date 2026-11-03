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
@ApiModel(value = "запрос получить клиента")
public class ClientRequestDTO {

    @NotNull
    @Size(max = 256)
    @ApiModelProperty("Customer identifier")
    private String customerId;

    @Size(max = 256)
    @ApiModelProperty("CardSuite bank identifier,MB_STD by default")
    private String bankId;
}
