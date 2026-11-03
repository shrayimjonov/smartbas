package uz.davrbank.repository.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import uz.davrbank.entity.CreateTest;
import uz.davrbank.entity.Department;
import uz.davrbank.entity.QuestionText;

public class CreateTestSpecification {

    public static Specification<CreateTest> findByDepartment(Department department) {
        return (Root<CreateTest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (department == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("department"), department);
        };
    }

    public static Specification<CreateTest> findByDepartmentId(Long departmentId) {
        return (Root<CreateTest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (departmentId == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("department").get("id"), departmentId);
        };
    }

    public static Specification<CreateTest> filterByLanguage(String language) {
        return (Root<CreateTest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (language == null) return criteriaBuilder.conjunction();
            Join<CreateTest, QuestionText> testQuestion = root.join("testQuestion", JoinType.LEFT);
            return criteriaBuilder.or(
                    criteriaBuilder.like(testQuestion.get("uz"), "%"), // For Uzbek
                    criteriaBuilder.like(testQuestion.get("ru"), "%")  // For Russian
            );
        };
    }

    public static Specification<CreateTest> hasValidQuestion(String language) {
        return (Root<CreateTest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<CreateTest, QuestionText> testQuestion = root.join("testQuestion", JoinType.LEFT);
            Join<CreateTest, QuestionText> questionA = root.join("questionA", JoinType.LEFT);
            Join<CreateTest, QuestionText> questionB = root.join("questionB", JoinType.LEFT);
            Join<CreateTest, QuestionText> questionC = root.join("questionC", JoinType.LEFT);
            Join<CreateTest, QuestionText> questionD = root.join("questionD", JoinType.LEFT);

            if ("uz".equals(language)) {
                return criteriaBuilder.or(
                        criteriaBuilder.isNotNull(testQuestion.get("uz")),
                        criteriaBuilder.isNotNull(questionA.get("uz")),
                        criteriaBuilder.isNotNull(questionB.get("uz")),
                        criteriaBuilder.isNotNull(questionC.get("uz")),
                        criteriaBuilder.isNotNull(questionD.get("uz"))
                );
            } else if ("ru".equals(language)) {
                return criteriaBuilder.or(
                        criteriaBuilder.isNotNull(testQuestion.get("ru")),
                        criteriaBuilder.isNotNull(questionA.get("ru")),
                        criteriaBuilder.isNotNull(questionB.get("ru")),
                        criteriaBuilder.isNotNull(questionC.get("ru")),
                        criteriaBuilder.isNotNull(questionD.get("ru"))
                );
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<CreateTest> findByDepartmentAndLanguage(Long departmentId, String language) {
        return Specification.where(findByDepartmentId(departmentId))
                .and(filterByLanguage(language))
                .and(hasValidQuestion(language));
    }

    public static Specification<CreateTest> deleteTestByDepartmentIdAndId(Long departmentId, Long id) {
        return (Root<CreateTest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate departmentPredicate = criteriaBuilder.equal(root.get("department").get("id"), departmentId);
            Predicate idPredicate = criteriaBuilder.equal(root.get("id"), id);
            return criteriaBuilder.and(departmentPredicate, idPredicate);
        };
    }
}
