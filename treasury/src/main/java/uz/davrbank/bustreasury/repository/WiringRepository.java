package uz.davrbank.bustreasury.repository;

import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.davrbank.bustreasury.aggregate.model.Template;
import uz.davrbank.bustreasury.aggregate.model.Wiring;

import java.util.List;
import java.util.Optional;

@Repository
public interface WiringRepository extends JpaRepository<Wiring, Long> {

    @Query("SELECT w FROM Wiring w WHERE w.wiringId = :wiringId")
    Optional<Wiring> findAllByWiringId(Long wiringId);

    Optional<Wiring> findByTemplateIdAndWiringId(Long templateId, Long wiringId);

    boolean existsByTemplateAndWiringId(Template template, Long wiringId);

    List<Wiring> findAllByTemplateId(Long templateId);

    List<Wiring> findAllByTemplateOrderByWiringId(Template template);
}
