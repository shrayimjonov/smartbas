package uz.davrbank.dto.pagination;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaginationRequest {
    private int page = 0;
    private int size = 10;
    private List<String> sortBy = List.of( "createdAt"); //"id", "percentage"

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
