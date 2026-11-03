package uz.davrbank.assignmentservice.aggregation.dto.general;

import java.util.List;

public record PageResponseDTO<T>(

        List<T> content,
        long totalElements,
        long totalPages,
        int page,
        int pageSize
) {
}
