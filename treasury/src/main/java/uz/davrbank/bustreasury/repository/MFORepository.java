package uz.davrbank.bustreasury.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.bustreasury.aggregate.model.MFO;

@Repository
public interface MFORepository extends JpaRepository<MFO, Long> {
}
