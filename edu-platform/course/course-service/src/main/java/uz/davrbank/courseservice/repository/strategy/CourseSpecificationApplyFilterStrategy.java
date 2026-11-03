package uz.davrbank.courseservice.repository.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uz.davrbank.courseservice.aggregation.filter.CourseFilter;
import uz.davrbank.courseservice.aggregation.model.CourseEmployeeAccess;
import uz.davrbank.courseservice.aggregation.model.course.Course;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseSpecificationApplyFilterStrategy implements SpecificationApplyFilterStrategy<Course, CourseFilter> {

    @Override
    public Specification<Course> applyFilter(CourseFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.conjunction();
            }

            List<String> roles = filter.getRoles() != null ? filter.getRoles() : List.of();
            boolean isEmployeeScope = roles.contains("ROLE_EMPLOYEE") || roles.contains("ROLE_DAVR");

            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), filter.getCategoryId()));
            }
            if (filter.getDepartmentId() != null && !isEmployeeScope) {
                predicates.add(cb.equal(root.get("departmentId"), filter.getDepartmentId()));
            }
            if (filter.getIsPublic() != null) {
                predicates.add(cb.equal(root.get("isPublic"), filter.getIsPublic()));
            }
            if (filter.getDeleted() != null) {
                predicates.add(cb.equal(root.get("deleted"), filter.getDeleted()));
            }
            if (filter.getTimeCompleteMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timeComplete"), filter.getTimeCompleteMin()));
            }
            if (filter.getTimeCompleteMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timeComplete"), filter.getTimeCompleteMax()));
            }
            if (filter.getScoreMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("score"), filter.getScoreMin()));
            }
            if (filter.getScoreMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("score"), filter.getScoreMax()));
            }
            if (filter.isSearchNotEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), filter.getSearchValue()));
            }

            applyAccessPredicateIfNeeded(filter, roles, isEmployeeScope, root, query, cb, predicates);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void applyAccessPredicateIfNeeded(CourseFilter filter,
                                              List<String> roles,
                                              boolean isEmployeeScope,
                                              Root<Course> root,
                                              CriteriaQuery<?> query,
                                              CriteriaBuilder cb,
                                              List<Predicate> predicates) {
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("roles is required");
        }
        if (roles.contains("ROLE_SUPERADMIN")) {
            return;
        }
        if (isEmployeeScope) {
            if (!StringUtils.hasText(filter.getEmployeeId())) {
                throw new IllegalArgumentException("employeeId is required");
            }
            if (filter.getDepartmentId() == null) {
                throw new IllegalArgumentException("departmentId is required");
            }
            Predicate accessPredicate = buildAccessPredicate(filter, root, query, cb);
            Predicate publicDepartmentPredicate = cb.and(
                    cb.isTrue(root.get("isPublic")),
                    cb.equal(root.get("departmentId"), filter.getDepartmentId())
            );
            predicates.add(cb.or(accessPredicate, publicDepartmentPredicate));
            return;
        }

        throw new RuntimeException("Роль не поддерживается");
    }

    private Predicate buildAccessPredicate(CourseFilter filter,
                                           Root<Course> root,
                                           CriteriaQuery<?> query,
                                           CriteriaBuilder cb) {
        Subquery<Long> accessSubquery = query.subquery(Long.class);
        Root<CourseEmployeeAccess> accessRoot = accessSubquery.from(CourseEmployeeAccess.class);
        accessSubquery.select(accessRoot.get("courseId"));
        accessSubquery.where(
                cb.equal(accessRoot.get("courseId"), root.get("id")),
                cb.equal(accessRoot.get("employeeId"), filter.getEmployeeId())
        );
        return cb.exists(accessSubquery);
    }
}
