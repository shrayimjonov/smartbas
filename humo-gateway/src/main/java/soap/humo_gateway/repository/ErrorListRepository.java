package soap.humo_gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soap.humo_gateway.entity.ErrorList;

@Repository
public interface ErrorListRepository extends JpaRepository<ErrorList, Long> {
    ErrorList findByCode(String code);
}
