package soap.humo_gateway.dto.request;

import lombok.Data;
import soap.humo_gateway.dto.enums.SubSystem;
import soap.humo_gateway.dto.annotations.CheckSubSystem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class DebitConfirmRequest {
    @NotBlank
    @Pattern(regexp = "^[0-9]*$")
    private String paymentId;
    @NotBlank
    @Pattern(regexp = "(wsdavr)|(wsdavr2)")
    private String login;
    @CheckSubSystem(message = "subsystem")
    private SubSystem subSystem;

}
