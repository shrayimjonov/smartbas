package nci.billing.repository;

import nci.billing.entity.Fraud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FraudRepository extends JpaRepository<Fraud, Long> {
    @Query(value = "select f from Fraud f where f.loanId=:loan and f.createdDate between current_date-1 and current_date")
    List<Fraud> getFraudByLoan(@Param("loan") String loan);

    @Query(value = "select f from Fraud f where f.paymentId=:paymentId and f.reason='SUCCESS'")
    List<Fraud> getFraudByPaymentId(@Param("paymentId") String paymentId);
}
