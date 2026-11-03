package nci.billing.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.annotation.Branch;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForexTranRequestDTO {
    @Branch
    private String branch;
    @NotBlank
    private String accountDebit;
    @NotBlank
    private String accountCredit;
    @NotNull
    @Min(1)
    private Long amount = 0L;
    @NotBlank
    @Pattern(regexp = "^\\d{3}$")
    private String currency;
    @NotNull
    private Long extId = 0L;
    @NotBlank
    private String purpose;

}
