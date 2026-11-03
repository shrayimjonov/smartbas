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
public class CustomerRemoveCardRequestDTO {

    @Size(max = 20)
    @JsonProperty(value = "Card")
    @ApiModelProperty("Card")
    private PanInfo card;
}
