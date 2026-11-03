package uz.devops.humoMiddleware.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "запрос получение карт клиентов по Персональному коду")
public class GetCardsByPinflRequestDTO {

    @ApiModelProperty("Personal code")
    @NotNull
    @Size(max = 20)
    @JsonProperty(value = "person_code")
    private String pinfl;
}
