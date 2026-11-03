package uz.davrbank.courseservice.repository.strategy;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.davrbank.courseservice.aggregation.filter.BaseFilter;

@Component
public interface SpecificationApplyFilterStrategy<T, F extends BaseFilter> {

    Specification<T> applyFilter(F filter);

    default Pageable buildPage(F filter) {
        return PageRequest.of(filter.getPage(), filter.getSize(), filter.getOrderedSortBy());
    }
}
