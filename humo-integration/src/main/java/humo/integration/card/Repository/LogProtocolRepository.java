package humo.integration.card.Repository;



import humo.integration.card.Entity.LogProtocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogProtocolRepository extends JpaRepository<LogProtocol, Long> {
}
