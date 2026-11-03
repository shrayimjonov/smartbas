package uz.davrbank.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.davrbank.courseservice.aggregation.model.step.Step;

import java.util.List;


@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

    List<Step> findAllByCourse_IdAndIsActive(Long courseId, Boolean active);

    @Modifying
    @Query("UPDATE Step s SET s.isActive = ?2 WHERE s.id = ?1")
    void updateIsActive(Long id, boolean active);
}