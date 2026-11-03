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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "запрос получение карт клиентов по паспортным данным")
public class GetCardsByPassportRequestDTO {
    @ApiModelProperty("Person identification document serial number")
    @NotNull
    @Size(max = 15)
    @JsonProperty(value = "serial_no")
    private String serial;

    @ApiModelProperty("Person identification document number")
    @NotNull
    @Size(max = 16)
    @JsonProperty(value = "id_card")
    private String number;
}
