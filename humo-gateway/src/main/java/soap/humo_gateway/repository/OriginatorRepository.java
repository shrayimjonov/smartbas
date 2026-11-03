package soap.humo_gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import soap.humo_gateway.entity.Originator;

import java.util.Optional;

@Repository
public interface OriginatorRepository extends JpaRepository<Originator, Long> {

    @Query(value = "select o from Originator o where o.bankName=:bankName")
    Optional<Originator> findByBankName(String bankName);
}
