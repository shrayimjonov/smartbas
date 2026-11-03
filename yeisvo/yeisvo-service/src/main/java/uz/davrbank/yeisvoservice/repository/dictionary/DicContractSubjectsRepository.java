package uz.davrbank.yeisvoservice.repository.dictionary;

import uz.davrbank.yeisvoservice.entity.spravochniki.ContractSubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DicContractSubjectsRepository extends JpaRepository<ContractSubjectsEntity, Long> {
    @Query("select e from ContractSubjectsEntity e where e.idNumber = :id")
    Optional<ContractSubjectsEntity> getByIdExt(@Param("id") Long id);
}
