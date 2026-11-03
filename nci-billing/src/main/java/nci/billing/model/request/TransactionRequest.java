package nci.billing.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.annotation.Branch;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    @NotBlank
    @Pattern(regexp = "^\\d{4,}$")
    private String loanId;
    @Branch
    private String branch;
    @NotBlank
    @Pattern(regexp = "\\d{2}.\\d{2}.\\d{4}")
    private String dateFrom;
}
