package uz.davrbank.bustreasury.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.bustreasury.aggregate.model.DocType;
import uz.davrbank.bustreasury.aggregate.model.Exchange;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
}
