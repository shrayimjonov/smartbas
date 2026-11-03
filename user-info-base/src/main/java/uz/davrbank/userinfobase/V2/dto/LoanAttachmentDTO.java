package uz.davrbank.userinfobase.V2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class LoanAttachmentDTO {

    UUID attachmentId;

    String branchId;

    String loanExtId;
}
