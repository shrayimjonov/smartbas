package uz.davrbank.payment.model.dto.billing.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckRequest {
    private UUID agentTranId;
    private String recipientId;
    private BigDecimal amount;
    private String account;

    public Map<Object, Object> toMap() {
        return Map.of(
                "agentTranId", this.agentTranId,
                "recipientId", this.recipientId,
                "amount", this.amount,
                "account", this.account
        );
    }
}
