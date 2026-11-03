package uz.davrbank.payment.model.dto.billing.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.payment.model.enums.PayType;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoldRequest {
    @JsonProperty("billId")
    private Long billId;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("type")
    private PayType type;
    @JsonProperty("params")
    private Map<String, String> params;

    public Map<Object, Object> toMap() {
        return Map.of(
                "billId", this.billId,
                "amount", this.amount,
                "type", this.type,
                "params", this.params
        );
    }

}
