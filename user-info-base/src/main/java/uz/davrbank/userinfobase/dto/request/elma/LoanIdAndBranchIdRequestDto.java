package uz.davrbank.userinfobase.dto.request.elma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanIdAndBranchIdRequestDto {
    @JsonProperty("LoanId")
    private String LoanId;

    @JsonProperty("branchId")
    private String branchId;
}
