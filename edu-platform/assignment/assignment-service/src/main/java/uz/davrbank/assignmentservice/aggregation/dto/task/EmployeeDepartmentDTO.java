package uz.davrbank.assignmentservice.aggregation.dto.task;

import java.util.List;

public record EmployeeDepartmentDTO(

        List<String> employeeId,
        List<Long> departmentId
) {
}
