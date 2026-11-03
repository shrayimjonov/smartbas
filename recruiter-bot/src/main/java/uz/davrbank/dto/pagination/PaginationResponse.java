package uz.davrbank.dto.pagination;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
@NoArgsConstructor
@ToString
@Builder
public class PaginationResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;



    public PaginationResponse(List<T> content, int totalPages, long totalElements, int page, int size) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
    }

    public static <T, U> PaginationResponse<U> fromPage(Page<T> pageData, Function<T, U> converter) {
        List<U> content = pageData.getContent().stream().map(converter).toList();
        return new PaginationResponse<>(
                content,
                pageData.getTotalPages(),
                pageData.getTotalElements(),
                pageData.getNumber(),
                pageData.getSize()
        );
    }

}
