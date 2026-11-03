package uz.davrbank.partnervalidationservice.common.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import uz.davrbank.partnervalidationservice.common.filter.BaseFilter;

public interface SpecificationApplyFilterStrategy {

    <T, F extends BaseFilter> Specification<T> applyFilter(Class<T> entityType, F filter);

    default <F extends BaseFilter> Pageable buildPage(F filter) {
        return PageRequest.of(filter.getPage(), filter.getSize(), filter.getOrderedSortBy());
    }
}
