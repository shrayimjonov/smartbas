package uz.davrbank.assignmentservice.repository.strategy;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.davrbank.assignmentservice.aggregation.filter.TypeFilter;
import uz.davrbank.assignmentservice.aggregation.model.course.Type;

import java.util.ArrayList;
import java.util.List;

@Component
public class TypeSpecificationApplyFilterStrategy implements SpecificationApplyFilterStrategy<Type, TypeFilter> {


    @Override
    public Specification<Type> applyFilter(TypeFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.isSearchNotEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), filter.getSearch()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}