package uz.davrbank.partnervalidationservice.merchant.query;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.davrbank.partnervalidationservice.common.query.AbstractSpecificationApplyFilterStrategy;
import uz.davrbank.partnervalidationservice.merchant.entity.Merchant;
import uz.davrbank.partnervalidationservice.merchant.filter.MerchantFilter;

@Component
public class MerchantSpecificationApplyFilterStrategy
        extends AbstractSpecificationApplyFilterStrategy<Merchant, MerchantFilter> {

    private static final List<String> SEARCHABLE_FIELDS = List.of("merchantExternalId", "name", "tin");

    public MerchantSpecificationApplyFilterStrategy() {
        super(Merchant.class, MerchantFilter.class);
    }

    @Override
    protected Specification<Merchant> buildSpec(MerchantFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getMerchantExternalId() != null && !filter.getMerchantExternalId().isBlank()) {
                predicates.add(cb.equal(root.get("merchantExternalId"), filter.getMerchantExternalId().trim()));
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
