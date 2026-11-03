package davr.yeisvo.repository.postgres.spravochniki;

import davr.yeisvo.entity.postgres.spravochniki.IndustrialZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndustrialZoneRepository extends JpaRepository<IndustrialZoneEntity, Long> {
    @Query("select e from IndustrialZoneEntity e where e.idNumber= :id")
    Optional<IndustrialZoneEntity> findByIdExt(@Param("id") Long id);

    boolean existsByIdNumber(Long idNumber);

}
