package nci.billing.model.request.loan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.annotation.Branch;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PaymentRequest {
    @NotBlank
    private String paymentId;
    @NotBlank
    @Branch
    private String branch;
    @Min(1000)
    private Integer formId = 0;
    @NotBlank
    private String cardNum;
    @NotBlank
    private String refId;

    private Terminal terminal;
    @Min(1)
    private Long amount = 0L;
    @Min(3636)
    private Integer originator = 0;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Terminal {
        @NotBlank
        private String terminalId;
        @NotBlank
        private String merchantId;
        @NotBlank
        @Pattern(regexp = "\\d{20}")
        private String account;
    }
}
