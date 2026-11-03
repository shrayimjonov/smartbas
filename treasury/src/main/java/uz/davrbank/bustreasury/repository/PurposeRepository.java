package uz.davrbank.bustreasury.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.bustreasury.aggregate.model.Purpose;

import java.util.Optional;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, Long> {
    boolean existsByCode(String code);

    Optional<Purpose> findByCode(String code);
}
