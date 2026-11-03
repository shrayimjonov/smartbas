package uz.davrbank.yeisvoservice.repository;

import uz.davrbank.yeisvoservice.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
    Optional<ContractEntity> findByIdn(String idn);
    Optional<ContractEntity> findByBankIdAndIdn(String bankId, String idn);





}
