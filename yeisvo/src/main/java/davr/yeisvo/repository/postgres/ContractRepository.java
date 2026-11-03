package davr.yeisvo.repository.postgres;

import davr.yeisvo.entity.postgres.ContractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
    Optional<ContractEntity> findByIdn(String idn);
    Optional<ContractEntity> findByBankIdAndIdn(String bankId, String idn);





}
