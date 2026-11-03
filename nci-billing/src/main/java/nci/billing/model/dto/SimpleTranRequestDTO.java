package nci.billing.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.annotation.Branch;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTranRequestDTO {
    @NotBlank
    private String accountDebit;
    @Branch
    private String branchDebit;
    @NotBlank
    private String accountCredit;
    @Branch
    private String branchCredit;
    @NotNull
    @Min(1)
    private Long amount = 0L;
    @NotBlank
    private String purpose;
    @NotNull
    private Long extId = 0L;


}
