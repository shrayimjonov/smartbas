package uz.davrbank.partnervalidationservice.merchant.query;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.davrbank.partnervalidationservice.common.query.AbstractSpecificationApplyFilterStrategy;
import uz.davrbank.partnervalidationservice.merchant.entity.MerchantAccount;
import uz.davrbank.partnervalidationservice.merchant.filter.MerchantAccountFilter;

@Component
public class MerchantAccountSpecificationApplyFilterStrategy
        extends AbstractSpecificationApplyFilterStrategy<MerchantAccount, MerchantAccountFilter> {

    private static final List<String> SEARCHABLE_FIELDS = List.of("branchCode", "accountNumber", "absClientId");

    public MerchantAccountSpecificationApplyFilterStrategy() {
        super(MerchantAccount.class, MerchantAccountFilter.class);
    }

    @Override
    protected Specification<MerchantAccount> buildSpec(MerchantAccountFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getMerchantId() != null) {
                predicates.add(cb.equal(root.get("merchantId"), filter.getMerchantId()));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getCurrency() != null) {
                predicates.add(cb.equal(root.get("currency"), filter.getCurrency()));
            }

            if (filter.isSearchNotEmpty()) {
                String searchValue = filter.getSearchValue();
                Predicate[] searchPredicates = SEARCHABLE_FIELDS.stream()
                        .map(field -> cb.like(cb.lower(root.get(field)), searchValue))
                        .toArray(Predicate[]::new);
                predicates.add(cb.or(searchPredicates));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
