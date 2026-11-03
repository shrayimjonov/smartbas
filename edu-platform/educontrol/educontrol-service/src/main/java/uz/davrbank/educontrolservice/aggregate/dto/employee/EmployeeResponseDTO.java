package uz.davrbank.educontrolservice.aggregate.dto.employee;


import com.fasterxml.jackson.annotation.JsonProperty;

public record EmployeeResponseDTO(

        @JsonProperty("id")
        String id,

        @JsonProperty("email")
        String email,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("username")
        String username,

        @JsonProperty("status")
        Boolean status,

        @JsonProperty("department_name")
        String departmentName,

        @JsonProperty("department_id")
        Long departmentId
) {
}
