package uz.davrbank.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uz.davrbank.courseservice.aggregation.model.step.StepEmployee;

@Repository
public interface StepEmployeeRepository extends JpaRepository<StepEmployee, Long>, JpaSpecificationExecutor<StepEmployee> {

    StepEmployee findByStepIdAndEmployeeIdAndIsCompleted(Long stepId, String employeeId, boolean active);
}
