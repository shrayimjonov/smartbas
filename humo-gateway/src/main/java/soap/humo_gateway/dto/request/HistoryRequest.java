package soap.humo_gateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soap.humo_gateway.dto.annotations.HumoRegex;
import soap.humo_gateway.dto.enums.SubSystem;
import soap.humo_gateway.dto.annotations.CheckSubSystem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRequest {
    @NotBlank
    @HumoRegex
    private String pan;
    @NotBlank
    @Pattern(regexp = "\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])")
    private String beginDate;
    @NotBlank
    @Pattern(regexp = "\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])")
    private String finishDate;
    @CheckSubSystem(message = "subsystem")
    private SubSystem subSystem;

}
