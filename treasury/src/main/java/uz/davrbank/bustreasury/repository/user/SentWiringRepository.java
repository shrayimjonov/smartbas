package uz.davrbank.bustreasury.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.davrbank.bustreasury.aggregate.model.user.SentWiring;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SentWiringRepository extends JpaRepository<SentWiring, Long> {
    long countByOperationsTemplateIdAndCreatedDateBetweenAndErrorCode(String string, LocalDateTime localDateTime, LocalDateTime localDateTime1, String number);

    boolean existsByPaymentId(Long id);

    void deleteByWiringId(Long id);

    @Query("SELECT s FROM SentWiring s WHERE s.documentNumber IS NOT NULL ORDER BY CAST(s.documentNumber AS INTEGER) DESC LIMIT 1")
    Optional<SentWiring> findTopByOrderByDocumentNumberDesc();

    Optional<SentWiring> findTopByWiring_WiringIdAndTransIdOrderByCreatedDateDesc(Long wiringId, String transId);

    List<SentWiring> findByTransId(String transId);

    List<SentWiring> findAllByOperations_IdOrderByCreatedDateAsc(Long operationId);

}

