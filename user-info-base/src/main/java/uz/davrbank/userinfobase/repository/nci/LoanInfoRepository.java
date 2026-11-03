package uz.davrbank.userinfobase.repository.nci;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.userinfobase.entity.nci.LoanInfoEntity;

import java.util.Optional;


@Repository
public interface LoanInfoRepository extends JpaRepository<LoanInfoEntity, Long> {
    Optional<LoanInfoEntity> findFirstByLoanExtId(String loanExtId);

    Optional<LoanInfoEntity> findByLoanId(@Param("loanId") String loanId);

    Optional<LoanInfoEntity> findFirstByLoanId(String loanId);

    Optional<LoanInfoEntity> findFirstByLoanExtIdAndBranchId(String loanExtId, String branchId);
}
