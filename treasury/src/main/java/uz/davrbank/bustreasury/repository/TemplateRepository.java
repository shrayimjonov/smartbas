package uz.davrbank.bustreasury.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.bustreasury.util.CurrencyType;
import uz.davrbank.bustreasury.aggregate.model.Template;
import uz.davrbank.bustreasury.util.OperationType;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    boolean existsByName(String name);

    long countByCurrency(CurrencyType currency);

    Optional<Template> findByCurrencyAndOperation(CurrencyType currencyType, OperationType operationType);

    @Query("select t from Template t where t.currency=:ccy and t.operation=:op")
    List<Template> findAllByCcyOp(@Param("ccy") CurrencyType ccy , @Param("op") OperationType op);
}
