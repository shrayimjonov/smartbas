package soap.humo_gateway.dto.request.limits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soap.humo_gateway.dto.annotations.CheckSubSystem;
import soap.humo_gateway.dto.annotations.HumoRegex;
import soap.humo_gateway.dto.enums.SubSystem;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCardLimitBalanceRequest {
    @HumoRegex
    private String card;
    @NotBlank
    private String type;
    @CheckSubSystem
    private SubSystem subSystem;
}
