package uz.davrbank.yeisvoservice.repository.dictionary;

import uz.davrbank.yeisvoservice.entity.spravochniki.RegionsEntity;
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
