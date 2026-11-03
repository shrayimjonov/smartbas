package davr.yeisvo.repository.postgres;

import davr.yeisvo.entity.postgres.BankBranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankBranchRepository extends JpaRepository<BankBranchEntity, Long> {


    boolean existsByInn(String inn);
    boolean existsByMfoAndInn(String mfo, String inn);


}
