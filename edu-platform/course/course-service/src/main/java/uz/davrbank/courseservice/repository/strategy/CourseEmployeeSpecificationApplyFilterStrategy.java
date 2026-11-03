package uz.davrbank.courseservice.repository.strategy;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.davrbank.courseservice.aggregation.filter.CourseEmployeeFilter;
import uz.davrbank.courseservice.aggregation.model.course.CourseEmployee;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseEmployeeSpecificationApplyFilterStrategy
        implements SpecificationApplyFilterStrategy<CourseEmployee, CourseEmployeeFilter> {

    @Override
    public Specification<CourseEmployee> applyFilter(CourseEmployeeFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.conjunction();
            }

            if (filter.getCourseId() != null) {
                predicates.add(cb.equal(root.get("courseId"), filter.getCourseId()));
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
