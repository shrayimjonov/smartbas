package uz.davrbank.educontrolservice.aggregate.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;


public record EmployeeDepartmentRequestDTO(

        @JsonProperty("employee_id")
        String employeeId,

        @JsonProperty("department_id")
        Long departmentId,

        @JsonProperty("position_id")
        Long positionId

) {


}