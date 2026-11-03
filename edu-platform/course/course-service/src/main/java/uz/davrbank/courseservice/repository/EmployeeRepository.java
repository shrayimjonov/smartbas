package uz.davrbank.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.courseservice.aggregation.model.Employee;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

     Optional<Employee> findFirstByEmployeeIdOrderByIdAsc(String employeeId);

}
