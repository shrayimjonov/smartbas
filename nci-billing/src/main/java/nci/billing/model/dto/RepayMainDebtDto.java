package nci.billing.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepayMainDebtDto {
    @NotBlank
    @Pattern(regexp = "\\d{9,}")
    private String loanExternal;
}
