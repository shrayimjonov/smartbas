package uz.davrbank.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.courseservice.aggregation.model.CourseEmployeeAccess;

import java.util.List;
import java.util.Set;

@Repository
public interface CurseEmployeeAccessRepository extends JpaRepository<CourseEmployeeAccess, Long> {

    Set<CourseEmployeeAccess> findByEmployeeId(String employeeId);

    Set<CourseEmployeeAccess> findByCourseId(Long courseId);

    @Modifying
    @Query("DELETE FROM CourseEmployeeAccess c WHERE c.courseId = :courseId AND c.employeeId IN :employeeIds")
    void deleteByCourseIdAndEmployeeId(@Param("courseId") Long courseId, @Param("employeeIds") List<String> employeeIds);

}
