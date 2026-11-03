package uz.davrbank.yeisvoservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.yeisvoservice.entity.ContractListEntity;

import java.util.Optional;

@Repository
public interface ContractGetListRepository extends JpaRepository<ContractListEntity,Long> {
     Optional<ContractListEntity> findByBankIdAndIdn(String bankId, String idn);

    @Query("""
    SELECT c FROM ContractListEntity c
    WHERE
        (:bankInn IS NULL OR :bankInn = '' OR c.bankInn = :bankInn)
    AND  (:bankId IS NULL OR :bankId = '' OR c.bankId = :bankId)
    AND  (:contractorUzInn IS NULL OR :contractorUzInn = '' OR c.contractorUzInn = :contractorUzInn)
    AND  (:docNo IS NULL OR :docNo = '' OR c.docNo = :docNo)
    AND  (:idn IS NULL OR :idn = '' OR c.idn = :idn)
    AND  (:contractorUzName IS NULL OR :contractorUzName = ''
    OR LOWER(c.contractorUzName) LIKE LOWER(CONCAT('%', :contractorUzName, '%')))
""")
    Page<ContractListEntity> searchContracts(
            @Param("bankInn") String bankInn,
            @Param("bankId") String bankId,
            @Param("contractorUzInn") String contractorUzInn,
            @Param("docNo") String docNo,
            @Param("idn") String idn,
            @Param("contractorUzName") String contractorUzName,
            Pageable pageable
    );

}
