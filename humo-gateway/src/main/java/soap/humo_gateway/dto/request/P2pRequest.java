package soap.humo_gateway.dto.request;

import lombok.Data;
import soap.humo_gateway.dto.annotations.HumoRegex;
import soap.humo_gateway.dto.enums.SubSystem;
import soap.humo_gateway.dto.annotations.CheckSubSystem;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class P2pRequest {

    @NotBlank
    private String refNum;
    @NotBlank
    @HumoRegex
    private String pan1;
    @NotBlank
    @Pattern(regexp = "(2\\d{3})|(3\\d{3})")
    private String expiry1;
    @NotBlank
    @HumoRegex
    private String pan2;
    @Min(1)
    private Long amount = 0L;
    @NotBlank
    private String merchantId;
    @NotBlank
    private String terminalId;
    @NotBlank
    @Pattern(regexp = "(wsdavr2)")
    private String login;
    @CheckSubSystem(message = "subsystem")
    private SubSystem subSystem;


}
