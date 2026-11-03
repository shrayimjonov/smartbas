package uz.davrbank.partnervalidationservice.common.query;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import uz.davrbank.partnervalidationservice.common.filter.BaseFilter;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractSpecificationApplyFilterStrategy<T, F extends BaseFilter>
        implements SpecificationApplyFilterStrategy {

    Class<T> entityType;
    Class<F> filterType;

    protected AbstractSpecificationApplyFilterStrategy(Class<T> entityType, Class<F> filterType) {
        this.entityType = entityType;
        this.filterType = filterType;
    }

    @Override
    public final <E, B extends BaseFilter> Specification<E> applyFilter(Class<E> entityType, B filter) {
        if (!this.entityType.equals(entityType) || !filterType.isInstance(filter)) {
            String filterName = filter == null ? "null" : filter.getClass().getName();
            throw new IllegalArgumentException("Unsupported filter " + filterName + " for entity " + entityType);
        }
        F typedFilter = filterType.cast(filter);
        Specification<T> spec = buildSpec(typedFilter);
        @SuppressWarnings("unchecked")
        Specification<E> casted = (Specification<E>) spec;
        return casted;
    }

    protected abstract Specification<T> buildSpec(F filter);
}
