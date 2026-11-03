package uz.davrbank.courseservice.aggregation.dto.general;

import java.util.List;

public record PageResponseDTO<T>(

        List<T> content,
        long totalElements,
        long totalPages,
        int page,
        int pageSize
) {
}
