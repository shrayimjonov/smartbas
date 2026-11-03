package davr.yeisvo.repository.postgres.spravochniki;

import davr.yeisvo.entity.postgres.spravochniki.BankInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface BankInfoRepository extends JpaRepository<BankInfoEntity, Long> {
    @Query("select e from BankInfoEntity e where e.id= :id")
    Optional<BankInfoEntity> getByIdExt(@Param("id") Long id);

    @Transactional
    Optional<BankInfoEntity> findByBankInnAndBankId(String bankInn, String bankId);

}
