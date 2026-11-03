package uz.davrbank.bustreasury.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.bustreasury.aggregate.model.user.Operations;

import java.util.Optional;

@Repository
public interface OperationsRepository extends JpaRepository<Operations,Long> {


    @EntityGraph(attributePaths = {})
    Page<Operations> findAll( Pageable pageable);

    Optional<Operations> findTopByTemplateIdOrderByDateDesc(String templateId);


    @Query("""
    SELECT o FROM Operations o
    LEFT JOIN o.sentWirings sw
    WHERE o.templateId = :templateId
    GROUP BY o
    ORDER BY MAX(sw.createdDate) DESC NULLS LAST, o.date DESC
    """)
    Page<Operations> findAllByTemplateIdOrderByLatestWiringDateDesc(@Param("templateId") String templateId,Pageable pageable);

    @Query("""
    SELECT o FROM Operations o
    LEFT JOIN o.sentWirings sw
    GROUP BY o
    ORDER BY MAX(sw.createdDate) DESC, o.date DESC
    """)
    Page<Operations> findAllByOrderByLatestWiringDateDesc(Pageable pageable);
}
