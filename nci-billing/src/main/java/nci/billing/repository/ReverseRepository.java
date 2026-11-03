package nci.billing.repository;

import nci.billing.entity.Reverse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReverseRepository extends JpaRepository<Reverse, Long> {
}
