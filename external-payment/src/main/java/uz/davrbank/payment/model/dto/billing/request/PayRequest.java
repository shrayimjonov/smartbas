package uz.davrbank.payment.model.dto.billing.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.payment.model.enums.PayType;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayRequest {
    private String billId;
    private PayType type;
    private Map<String, String> params;

    public Map<Object, Object> toMap() {
        return Map.of(
                "billId", this.billId,
                "type", this.type,
                "params", this.params
        );
    }
}
