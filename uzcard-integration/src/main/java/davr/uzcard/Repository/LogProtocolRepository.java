package davr.uzcard.Repository;


import davr.uzcard.Entity.LogProtocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogProtocolRepository extends JpaRepository<LogProtocol, Long> {
}
