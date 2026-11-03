package uz.davrbank.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uz.davrbank.courseservice.aggregation.model.course.CourseEmployee;

@Repository
public interface CurseEmployeeRepository extends JpaRepository<CourseEmployee, Long>, JpaSpecificationExecutor<CourseEmployee> {

    CourseEmployee findByCourseIdAndEmployeeIdAndIsCompleted(Long courseId, String employeeId, boolean active);
}
