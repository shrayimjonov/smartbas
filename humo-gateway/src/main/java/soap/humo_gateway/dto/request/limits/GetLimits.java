package soap.humo_gateway.dto.request.limits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soap.humo_gateway.dto.annotations.CheckSubSystem;
import soap.humo_gateway.dto.enums.SubSystem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLimits {
    @CheckSubSystem
    private SubSystem subSystem;
}
