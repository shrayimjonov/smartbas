package soap.humo_gateway.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soap.humo_gateway.entity.LogProtocol;

@Repository
public interface LogRepository extends JpaRepository<LogProtocol, Long> {
}
