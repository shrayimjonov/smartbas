package uz.davrbank.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import uz.davrbank.entity.Message;

public class MessageSpecification {

    public static Specification<Message> searchByLanguageAndType(String language, String type, String text) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(language) || !StringUtils.hasText(type) || !StringUtils.hasText(text)) {
                return criteriaBuilder.conjunction(); // Always true if input is invalid
            }

            String lowerCaseText = "%" + text.toLowerCase() + "%";
            switch (language) {
                case "uz":
                    if ("description".equals(type)) {
                        return criteriaBuilder.like(criteriaBuilder.lower(root.get("description").get("uz")), lowerCaseText);
                    } else if ("title".equals(type)) {
                        return criteriaBuilder.like(criteriaBuilder.lower(root.get("title").get("uz")), lowerCaseText);
                    }
                    break;
                case "ru":
                    if ("description".equals(type)) {
                        return criteriaBuilder.like(criteriaBuilder.lower(root.get("description").get("ru")), lowerCaseText);
                    } else if ("title".equals(type)) {
                        return criteriaBuilder.like(criteriaBuilder.lower(root.get("title").get("ru")), lowerCaseText);
                    }
                    break;
                case "en":
                    if ("description".equals(type)) {
                        return criteriaBuilder.like(criteriaBuilder.lower(root.get("description").get("en")), lowerCaseText);
                    } else if ("title".equals(type)) {
                        return criteriaBuilder.like(criteriaBuilder.lower(root.get("title").get("en")), lowerCaseText);
                    }
                    break;
                default:
                    return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Message> orderByIdDesc() {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            query.orderBy(criteriaBuilder.desc(root.get("id")));
            return criteriaBuilder.conjunction();
        };
    }
}
