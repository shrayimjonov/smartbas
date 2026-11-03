package soap.humo_gateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soap.humo_gateway.dto.annotations.CheckSubSystem;
import soap.humo_gateway.dto.annotations.HumoRegex;
import soap.humo_gateway.dto.enums.SubSystem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPinReq {
    @HumoRegex
    private String card;
    @NotBlank
    @Pattern(regexp = "(2\\d{3})|(3\\d{3})")
    private String expiry;
    @NotBlank
    private String text;
    @CheckSubSystem
    private SubSystem subSystem;
}
