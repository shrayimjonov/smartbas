package uz.davrbank.courseservice.aggregation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee_ids")
public class Employee {
    @Id
    private Long id;

    private String employeeId;
    private String firstName;
    private String lastName;

    public Employee(String employeeId) {
        this.employeeId = employeeId;
    }
}
