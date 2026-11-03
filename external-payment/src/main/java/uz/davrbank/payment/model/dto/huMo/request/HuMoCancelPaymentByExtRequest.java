package uz.davrbank.payment.model.dto.huMo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuMoCancelPaymentByExtRequest {
    private String login;
    private String refNumber;
    private String subSystem;
}
