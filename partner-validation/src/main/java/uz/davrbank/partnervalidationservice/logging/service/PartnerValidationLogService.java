package uz.davrbank.partnervalidationservice.logging.service;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.partnervalidationservice.common.api.ResponsePageDTO;
import uz.davrbank.partnervalidationservice.common.query.PageResponseMapper;
import uz.davrbank.partnervalidationservice.logging.dto.PartnerValidationLogListDTO;
import uz.davrbank.partnervalidationservice.logging.entity.PartnerValidationLog;
import uz.davrbank.partnervalidationservice.logging.filter.PartnerValidationLogFilter;
import uz.davrbank.partnervalidationservice.logging.query.PartnerValidationLogSpecificationApplyFilterStrategy;
import uz.davrbank.partnervalidationservice.logging.repository.PartnerValidationLogRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PartnerValidationLogService {

    private static final Map<String, String> ORDER_BY_MAPPING = Map.of(
            "id", "id",
            "correlationId", "correlationId",
            "monitoringStep", "monitoringStep",
            "methodType", "methodType",
            "url", "url",
            "requestDate", "requestDate",
            "responseDate", "responseDate",
            "ipAddress", "ipAddress",
            "success", "success"
    );

    PartnerValidationLogRepository partnerValidationLogRepository;
    PartnerValidationLogSpecificationApplyFilterStrategy strategy;

    public ResponsePageDTO<PartnerValidationLogListDTO> getPageable(PartnerValidationLogFilter filter) {
        PartnerValidationLogFilter effectiveFilter = resolveFilter(filter);
        Page<PartnerValidationLogListDTO> page = partnerValidationLogRepository.findAll(
                        strategy.applyFilter(PartnerValidationLog.class, effectiveFilter),
                        buildPage(effectiveFilter)
                )
                .map(this::toListDTO);
        return PageResponseMapper.fromPage(page);
    }

    private PartnerValidationLogFilter resolveFilter(PartnerValidationLogFilter filter) {
        return filter == null ? new PartnerValidationLogFilter() : filter;
    }

    private PartnerValidationLogListDTO toListDTO(PartnerValidationLog log) {
        return new PartnerValidationLogListDTO(
                log.getId(),
                log.getCorrelationId(),
                log.getMonitoringStep(),
                log.getRequest(),
                log.getResponse(),
                log.getMethodType(),
                log.getUrl(),
                log.getRequestDate(),
                log.getResponseDate(),
                log.getIpAddress(),
                log.isSuccess()
        );
    }

    private PageRequest buildPage(PartnerValidationLogFilter filter) {
        String orderBy = ORDER_BY_MAPPING.getOrDefault(filter.getOrderBy(), "requestDate");
        Sort.Direction direction = "asc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(direction, orderBy));
    }
}
