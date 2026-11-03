package davr.yeisvo.repository.postgres;

import davr.yeisvo.entity.postgres.JuridicINNEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface JuridicINNRepository extends JpaRepository<JuridicINNEntity, Long> {
    boolean existsByClientcbIdAndBranchClient(int clientcbId, String branchClient);

        @Modifying
        @Transactional
        @Query("UPDATE JuridicINNEntity j SET " +
                "j.clientId = :#{#entity.clientId}, " +
                "j.companyName = :#{#entity.companyName}, " +
                "j.companyInn = :#{#entity.companyInn}, " +
                "j.statusClient = :#{#entity.statusClient}, " +
                "j.requestBankId = :#{#entity.requestBankId}, " +
                "j.requestActive = :#{#entity.requestActive} " +
                "WHERE j.branchClient = :#{#entity.branchClient} " +
                "AND j.clientcbId = :#{#entity.clientcbId}")
        int updateByBranchClientAndClientcbId(@Param("entity") JuridicINNEntity entity);
}