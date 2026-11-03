package uz.davrbank.userinfobase.dto.pagination;

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
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    @Builder.Default
    private List<String> sortBy = List.of("createdAt");

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
