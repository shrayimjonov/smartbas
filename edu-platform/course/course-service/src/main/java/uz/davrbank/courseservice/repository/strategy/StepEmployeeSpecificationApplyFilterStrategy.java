package uz.davrbank.courseservice.repository.strategy;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.davrbank.courseservice.aggregation.filter.StepEmployeeFilter;
import uz.davrbank.courseservice.aggregation.model.step.StepEmployee;

import java.util.ArrayList;
import java.util.List;

@Component
public class StepEmployeeSpecificationApplyFilterStrategy
        implements SpecificationApplyFilterStrategy<StepEmployee, StepEmployeeFilter> {

    @Override
    public Specification<StepEmployee> applyFilter(StepEmployeeFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.conjunction();
            }

            if (filter.getStepId() != null) {
                predicates.add(cb.equal(root.get("stepId"), filter.getStepId()));
            }
            if (filter.getEmployeeId() != null) {
                predicates.add(cb.equal(root.get("employeeId"), filter.getEmployeeId()));
            }
            if (filter.getIsCompleted() != null) {
                predicates.add(cb.equal(root.get("isCompleted"), filter.getIsCompleted()));
            }
            if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
                predicates.add(root.get("status").in(filter.getStatuses()));
            }
            if (filter.isSearchNotEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("employeeId")), filter.getSearchValue()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
