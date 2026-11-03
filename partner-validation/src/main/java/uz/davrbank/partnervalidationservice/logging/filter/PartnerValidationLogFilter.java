package uz.davrbank.partnervalidationservice.logging.filter;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import uz.davrbank.partnervalidationservice.common.filter.BaseFilter;
import uz.davrbank.partnervalidationservice.logging.enums.MonitoringStep;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerValidationLogFilter extends BaseFilter {

    UUID correlationId;

    String methodType;

    String url;

    String ipAddress;

    Boolean success;

    MonitoringStep monitoringStep;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime requestDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime requestDateTo;

    public PartnerValidationLogFilter() {
        setOrderBy("requestDate");
        setSortOrder("desc");
    }
}
