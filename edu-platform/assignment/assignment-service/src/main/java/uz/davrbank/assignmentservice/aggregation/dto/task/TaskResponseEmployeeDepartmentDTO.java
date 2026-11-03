package uz.davrbank.assignmentservice.aggregation.dto.task;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record TaskResponseEmployeeDepartmentDTO(

        Long id,
        String name,
        String startDate,
        String endDate,
        String description,
        String status,
        String priority
) {
}
