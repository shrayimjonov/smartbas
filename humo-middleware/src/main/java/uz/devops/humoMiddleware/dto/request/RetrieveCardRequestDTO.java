package uz.devops.humoMiddleware.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "запрос получение базовой карты")
public class RetrieveCardRequestDTO {

    @ApiModelProperty("PAN, card number")
    @NotNull
    @Size(max = 19)
    private String primaryAccountNumber;

    @ApiModelProperty("Use this field to get mobile banking status 0 or 1")
    @JsonProperty(value = "mb_flag")
    private Integer flag;
}
