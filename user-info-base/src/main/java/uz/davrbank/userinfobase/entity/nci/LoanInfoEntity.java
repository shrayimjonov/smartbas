package uz.davrbank.userinfobase.entity.nci;

import jakarta.persistence.*;
import lombok.*;
import uz.davrbank.userinfobase.entity.files.FileUpload;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "loan_info")
public class LoanInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanExtId;
    private String loanId;
    private String mfo;
    private String clientName;
    private String clientBirthday;
    private Long  mainAccBalance;
    private Long  transitBalance;
    private Long  principalBalance;
    private Long  arrearsOnPrincipal;
    private Long  ofOverdueDebt;
    private Long  interestOnTermDebt;
    private Long  ofOverdueInterest;
    private Long  arrearsOfInterest;
    private Long  overdueCovenantCommission;
    private Long  recommendedAmount;
    private Long  maxAmount;
    private Long  loanAmount;
    private String loanClose;
    private String loanOpen;
    private Long transitState;
    private String transitAccount;
    private String loanType;
    private Long  lossReserve;
    private String currency;
    private Long  courtCosts;
    private String branchId;


    @OneToMany(mappedBy = "loanInfo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FileUpload> fileUploads = new ArrayList<>();

}
