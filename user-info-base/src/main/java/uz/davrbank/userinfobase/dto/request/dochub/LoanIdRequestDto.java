package uz.davrbank.userinfobase.dto.request.dochub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanIdRequestDto {
    private String loanId;
}
