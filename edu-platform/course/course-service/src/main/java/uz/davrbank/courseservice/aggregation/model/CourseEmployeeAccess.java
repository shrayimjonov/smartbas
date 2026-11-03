package uz.davrbank.courseservice.aggregation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_employee_access")
public class CourseEmployeeAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeId;
    private Long courseId;

    public CourseEmployeeAccess(String employeeId, Long courseId) {
        this.employeeId = employeeId;
        this.courseId = courseId;
    }
}
