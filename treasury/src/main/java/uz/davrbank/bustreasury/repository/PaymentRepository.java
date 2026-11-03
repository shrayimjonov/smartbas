package uz.davrbank.bustreasury.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.davrbank.bustreasury.aggregate.model.Payment;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.code = :code ORDER BY p.id LIMIT 1")
    Optional<Payment> findByCode(String code);
}
