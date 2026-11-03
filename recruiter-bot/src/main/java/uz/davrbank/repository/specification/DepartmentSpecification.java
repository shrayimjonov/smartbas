package uz.davrbank.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import uz.davrbank.entity.Department;

public class DepartmentSpecification {
    public static Specification<Department> orderByIdDesc() {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            query.orderBy(criteriaBuilder.desc(root.get("id")));
            return criteriaBuilder.conjunction();
        };
    }
}
