package humo.integration.card.Model.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HumoPhoneDto implements Serializable {
    @NotBlank(message = "Phone field is mandatory")
    @ApiModelProperty(value = "Set Customer Phone number with +")
    private String phone;
}
