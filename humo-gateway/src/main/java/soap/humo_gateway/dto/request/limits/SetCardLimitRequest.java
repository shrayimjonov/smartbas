package soap.humo_gateway.dto.request.limits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soap.humo_gateway.dto.annotations.CheckSubSystem;
import soap.humo_gateway.dto.annotations.HumoRegex;
import soap.humo_gateway.dto.enums.SubSystem;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetCardLimitRequest {
    @HumoRegex
    private String card;
    private List<Map<String, String>> limits;
    @CheckSubSystem
    private SubSystem subSystem;
}
