package davr.uzcard.Repository;

import davr.uzcard.Entity.Params;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceParamsRepository extends JpaRepository<Params, Long> {
    @Query(value = "select p from Params p where p.name=:name")
    Optional<Params> getParam(String name);
}
