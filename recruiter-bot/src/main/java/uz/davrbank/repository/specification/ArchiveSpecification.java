package uz.davrbank.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.davrbank.entity.Archive;

import java.util.ArrayList;
import java.util.List;

public class ArchiveSpecification {

    public static Specification<Archive> searchByLanguageAndType(String language, String type, String text) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if ("uz".equalsIgnoreCase(language)) {
                if ("description".equalsIgnoreCase(type)) {
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description").get("uz")),
                            "%" + text.toLowerCase() + "%"
                    ));
                } else if ("title".equalsIgnoreCase(type)) {
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("title").get("uz")),
                            "%" + text.toLowerCase() + "%"
                    ));
                }
            } else if ("ru".equalsIgnoreCase(language)) {
                if ("description".equalsIgnoreCase(type)) {
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description").get("ru")),
                            "%" + text.toLowerCase() + "%"
                    ));
                } else if ("title".equalsIgnoreCase(type)) {
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("title").get("ru")),
                            "%" + text.toLowerCase() + "%"
                    ));
                }
            } else if ("en".equalsIgnoreCase(language)) {
                if ("description".equalsIgnoreCase(type)) {
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description").get("en")),
                            "%" + text.toLowerCase() + "%"
                    ));
                } else if ("title".equalsIgnoreCase(type)) {
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("title").get("en")),
                            "%" + text.toLowerCase() + "%"
                    ));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Archive> orderByIdDesc() {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            query.orderBy(criteriaBuilder.desc(root.get("id")));
            return null;
        };
    }
}
