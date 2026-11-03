package davr.uzcard.Repository;

import davr.uzcard.Entity.CardAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardActionRepository extends JpaRepository<CardAction,Long> {
}
