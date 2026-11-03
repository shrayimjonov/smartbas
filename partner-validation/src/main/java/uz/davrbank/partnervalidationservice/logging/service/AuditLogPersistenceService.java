package uz.davrbank.partnervalidationservice.logging.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.partnervalidationservice.logging.entity.PartnerValidationLog;
import uz.davrbank.partnervalidationservice.logging.enums.MonitoringStep;
import uz.davrbank.partnervalidationservice.logging.repository.PartnerValidationLogRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogPersistenceService {

    PartnerValidationLogRepository partnerValidationLogRepository;
    ObjectMapper objectMapper;

    @Async("auditTaskExecutor")
    @Transactional
    public void persistValidationStepAsync(
            MonitoringStep monitoringStep,
            Object requestPayload,
            Object responsePayload,
            String methodType,
            String url,
            boolean success,
            UUID correlationId,
            String ipAddress
    ) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        PartnerValidationLog logEntry = PartnerValidationLog.builder()
                .id(UUID.randomUUID())
                .correlationId(correlationId)
                .monitoringStep(monitoringStep)
                .request(writeAsJson(requestPayload))
                .response(writeAsJson(responsePayload))
                .methodType(methodType)
                .url(url)
                .requestDate(now)
                .responseDate(now)
                .ipAddress(ipAddress)
                .success(success)
                .build();
        partnerValidationLogRepository.save(logEntry);
    }

    private String writeAsJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize audit payload", exception);
        }
    }
}
