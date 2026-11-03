package nci.billing.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayRequest {
    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("loan_id")
    private String loanId;
    @JsonProperty("terminal_id")
    private String terminalId;
    @JsonProperty("nspc_trans_id")
    private String paymentId;
    @JsonProperty("amount")
    private Long amount;
    @JsonProperty("interest")
    private Long interest;
    @JsonProperty("payment_type")
    private int paymentType;
    @JsonProperty("un")
    private String username;
    @JsonProperty("pwd")
    private String password;
    @JsonProperty("payment_src")
    private String paymentSrc;


}
