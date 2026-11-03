package soap.humo_gateway.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import soap.humo_gateway.entity.Bins;

import java.util.List;
import java.util.Optional;

@Repository
public interface BinsRepository extends JpaRepository<Bins, Long> {
    Optional<Bins> findBinsByBinCode(String bin);

    @Query(value = "select b from Bins b where b.binCode=:cardPan")
    Optional<Bins> findCentreIdByPan(String cardPan);

    @Query(value = "select b from Bins b where upper(b.cardName) like '%VISA%'")
    List<Bins> findVisaBins();

    @Query(value = "select b from Bins b where b.bankCode='25' and upper(b.cardName) not like '%VISA%'")
    List<Bins> findDavrBinsNotVisa();

    @Query(value = "select b from Bins b where b.bankCode='25'")
    List<Bins> findDavrBins();
}
