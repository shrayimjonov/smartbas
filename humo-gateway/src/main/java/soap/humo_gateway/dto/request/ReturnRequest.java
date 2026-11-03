package soap.humo_gateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soap.humo_gateway.dto.enums.SubSystem;
import soap.humo_gateway.dto.annotations.CheckSubSystem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest {
    @NotBlank
    @Pattern(regexp = "^[0-9]*$")
    private String paymentId;
    @NotBlank
    private String merchantId;
    @NotBlank
    private String terminalId;
    @NotBlank
    @Pattern(regexp = "(wsdavr)|(wsdavr2)")
    private String login;
    @CheckSubSystem(message = "subsystem")
    private SubSystem subSystem;
}
