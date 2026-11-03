package nci.billing.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.annotation.System;
import nci.billing.model.enums.SystemType;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPaymentDto {
    @NotBlank
    @Pattern(regexp = "^[0-9]*$", message = "request Id must be consist from digits")
    private String requestId;
    @System(message = "please contact with your administrator about systems")
    private SystemType system;
}
