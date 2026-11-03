package davr.yeisvo.repository.postgres.spravochniki;

import davr.yeisvo.entity.postgres.spravochniki.RegionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionsRepository extends JpaRepository<RegionsEntity, Long> {
    @Query("select e from RegionsEntity e where e.idNumber = :id")
    Optional<RegionsEntity> findByIdExt(@Param("id") String id);
}
