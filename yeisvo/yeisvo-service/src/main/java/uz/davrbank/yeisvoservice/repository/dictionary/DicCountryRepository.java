package uz.davrbank.yeisvoservice.repository.dictionary;

import uz.davrbank.yeisvoservice.entity.spravochniki.DicCountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DicCountryRepository extends JpaRepository<DicCountryEntity, Long> {
    @Query("select c from DicCountryEntity c where c.idNumber = :id")
    Optional<DicCountryEntity> findByIdExt(@Param("id") String id);

}
