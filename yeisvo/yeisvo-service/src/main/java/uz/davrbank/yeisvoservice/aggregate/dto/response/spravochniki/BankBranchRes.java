package uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.yeisvoservice.aggregate.dto.response.BaseResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankBranchRes extends BaseResponse {
    private String bankId; // Mfo, bxm kod, bxo kod
    private String bankType;
    private String regionId;
    private String headerId;
    private String unionId;
    private String tccId;
    private String cccId;
    private String bankName;
    private String bankAddress;
    private String bankStatus;
    private String active;
    private String dateOpen;
    private String dateClose;
    private String district;
    private String kolObm;
    private String inn;
    private String branchType;  /* BRANCH_TYPE=00001 => 'Branch', BRANCH_TYPE<>00001 => 'Subbranch' */
}
