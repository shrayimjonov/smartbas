package soap.humo_gateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soap.humo_gateway.dto.annotations.CheckSubSystem;
import soap.humo_gateway.dto.annotations.HumoRegex;
import soap.humo_gateway.dto.enums.SubSystem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceRequest {
    @HumoRegex
    private String card;
    @CheckSubSystem
    private SubSystem subSystem;
}
