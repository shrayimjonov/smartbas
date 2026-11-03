package uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsPageResponse<T> extends AbsListResponse<T> {
    private Long totalElements;
    private Integer totalPages;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer numberOfElements;
    private Boolean first;
    private Boolean last;
    private Boolean empty;
}
