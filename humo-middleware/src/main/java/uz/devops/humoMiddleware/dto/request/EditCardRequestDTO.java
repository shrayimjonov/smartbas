package uz.devops.humoMiddleware.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.basic.Card;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "запрос редактировать карточку клиента")
public class EditCardRequestDTO {
    @JsonProperty("Card")
    @ApiModelProperty("Customer card")
    private Card card;
}
