package uz.davrbank.userinfobase.repository.nci;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.davrbank.userinfobase.entity.nci.LoanInfoEntity;
import uz.davrbank.userinfobase.entity.nci.PersonDetails;

import java.util.ArrayList;
import java.util.List;


public class PersonalDetailsSpecification {

    public static Specification<PersonDetails> filterByPinfl(String pinfl) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (pinfl != null && !pinfl.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("pinfl"), pinfl + "%"));
            }
            assert query != null;
            query.orderBy(criteriaBuilder.desc(root.get("id")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<PersonDetails> orderByIdDesc() {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            query.orderBy(criteriaBuilder.desc(root.get("id")));
            return criteriaBuilder.conjunction();
        };
    }
    public static Specification<PersonDetails> filterByLoanId(String loanId) {
        return (root, query, criteriaBuilder) -> {
            if (loanId != null && !loanId.isEmpty()) {
                Join<PersonDetails, LoanInfoEntity> loanJoin = root.join("loanInfoEntities", JoinType.INNER);
                return criteriaBuilder.equal(loanJoin.get("loanId"), loanId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<PersonDetails> filterByPinflAndLoanIdAndOrderByIdDesc(String pinfl, String loanId) {
        return Specification.where(filterByPinfl(pinfl))
                .and(filterByLoanId(loanId))
                .and(orderByIdDesc());
    }

}
