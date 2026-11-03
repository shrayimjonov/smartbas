package uz.davrbank.partnervalidationservice.logging.query;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uz.davrbank.partnervalidationservice.common.query.AbstractSpecificationApplyFilterStrategy;
import uz.davrbank.partnervalidationservice.logging.entity.PartnerValidationLog;
import uz.davrbank.partnervalidationservice.logging.filter.PartnerValidationLogFilter;

@Component
public class PartnerValidationLogSpecificationApplyFilterStrategy
        extends AbstractSpecificationApplyFilterStrategy<PartnerValidationLog, PartnerValidationLogFilter> {

    public PartnerValidationLogSpecificationApplyFilterStrategy() {
        super(PartnerValidationLog.class, PartnerValidationLogFilter.class);
    }

    @Override
    protected Specification<PartnerValidationLog> buildSpec(PartnerValidationLogFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getCorrelationId() != null) {
                predicates.add(cb.equal(root.get("correlationId"), filter.getCorrelationId()));
            }

            if (filter.getMonitoringStep() != null) {
                predicates.add(cb.equal(root.get("monitoringStep"), filter.getMonitoringStep()));
            }

            if (filter.getMethodType() != null && !filter.getMethodType().isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("methodType")),
                        filter.getMethodType().trim().toLowerCase()
                ));
            }

            if (filter.getUrl() != null && !filter.getUrl().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("url")),
                        "%" + filter.getUrl().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getIpAddress() != null && !filter.getIpAddress().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("ipAddress")),
                        "%" + filter.getIpAddress().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getSuccess() != null) {
                predicates.add(cb.equal(root.get("success"), filter.getSuccess()));
            }

            if (filter.getRequestDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("requestDate"), filter.getRequestDateFrom()));
            }

            if (filter.getRequestDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("requestDate"), filter.getRequestDateTo()));
            }

            if (filter.isSearchNotEmpty()) {
                String searchValue = filter.getSearchValue();
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("methodType")), searchValue),
                        cb.like(cb.lower(root.get("url")), searchValue),
                        cb.like(cb.lower(root.get("ipAddress")), searchValue),
                        cb.like(cb.lower(root.get("monitoringStep").as(String.class)), searchValue),
                        cb.like(cb.lower(root.get("correlationId").as(String.class)), searchValue),
                        cb.like(cb.lower(root.get("request")), searchValue),
                        cb.like(cb.lower(root.get("response")), searchValue)
                ));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
