package uz.davrbank.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.davrbank.courseservice.aggregation.model.step.StepTextBlock;

import java.util.List;

public interface StepTextBlockRepository extends JpaRepository<StepTextBlock, Long> {

    List<StepTextBlock> findAllByStepId(Long stepId);

    @Query("SELECT MAX(s.orderInStep) FROM StepTextBlock s WHERE s.step.id = :stepId")
    Integer findMaxOrderInStep(@Param("stepId") Long stepId);




}
