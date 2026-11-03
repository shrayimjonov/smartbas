package uz.davrbank.userinfobase.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalFileRequestDto {

    private String loanId;

    private String branchId;

}
