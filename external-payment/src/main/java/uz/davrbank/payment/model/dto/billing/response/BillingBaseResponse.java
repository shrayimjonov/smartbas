package uz.davrbank.payment.model.dto.billing.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nodir.commondto.model.enums.ResponseStatus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingBaseResponse<R> {
    private Long id;
    private String recipientShortName;
    private Object sessionId;
    private BigDecimal amount;
    private String account;
    private Integer currency;
    private String extId;
    private String requestTime;
    private String responseTime;
    private String responseText;
    private Boolean success;
    private ResponseStatus status;
    private Map<String, Object> data;
    private Object gatewayName;
    private R sender;
    private R receiver;

    public BillingBaseResponse(String responseText, ResponseStatus status) {
        this.responseText = responseText;
        this.status = status;
    }

    public Map<Object, Object> toMap() {
        var map = new HashMap<>();
        map.put("id", this.id);
        map.put("recipientShortName", this.recipientShortName);
        map.put("sessionId", this.sessionId);
        map.put("amount", this.amount);
        map.put("account", this.account);
        map.put("currency", this.currency);
        map.put("extId", this.extId);
        map.put("requestTime", this.requestTime);
        map.put("responseTime", this.responseTime);
        map.put("responseText", this.responseText);
        map.put("success", this.success);
        map.put("status", this.status);
        map.put("data", this.data);
        map.put("gatewayName", this.gatewayName);
        map.put("sender", this.sender);
        map.put("receiver", this.receiver);
        return map;
    }
}
