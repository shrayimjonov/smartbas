package uz.davrbank.assignmentservice.repository.strategy;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.davrbank.assignmentservice.aggregation.filter.CategoryFilter;
import uz.davrbank.assignmentservice.aggregation.model.course.Category;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategorySpecificationApplyFilterStrategy implements SpecificationApplyFilterStrategy<Category, CategoryFilter> {


    @Override
    public Specification<Category> applyFilter(CategoryFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getTypeId() != null) {
                predicates.add(cb.equal(root.get("typeId"), filter.getTypeId()));
            }
            if (filter.isSearchNotEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), filter.getSearch()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}