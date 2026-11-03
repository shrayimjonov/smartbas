package uz.davrbank.bustreasury.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.bustreasury.aggregate.model.DocType;

@Repository
public interface DocTypeRepository extends JpaRepository<DocType, Long> {
    boolean existsByKodDoc(String kodDoc);
}
