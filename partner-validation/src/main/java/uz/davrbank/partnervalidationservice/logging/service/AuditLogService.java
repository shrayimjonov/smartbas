package uz.davrbank.partnervalidationservice.logging.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.davrbank.partnervalidationservice.constants.AuthConstants;
import uz.davrbank.partnervalidationservice.logging.enums.MonitoringStep;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogService {

    AuditLogPersistenceService auditLogPersistenceService;

    public void log(
            MonitoringStep monitoringStep,
            Object requestPayload,
            Object responsePayload,
            String methodType,
            String url,
            boolean success,
            UUID correlationId
    ) {
        String ipAddress = resolveRequest().map(this::resolveIpAddress).orElse("unknown");
        String resolvedUrl = resolveRequest().map(HttpServletRequest::getRequestURI).orElse(url);
        auditLogPersistenceService.persistValidationStepAsync(
                monitoringStep,
                requestPayload,
                responsePayload,
                methodType,
                resolvedUrl,
                success,
                correlationId,
                ipAddress
        );
    }

    private Optional<HttpServletRequest> resolveRequest() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return Optional.empty();
        }
        return Optional.of(attributes.getRequest());
    }

    private Optional<UUID> resolveCorrelationId() {
        return Optional.ofNullable(MDC.get(AuthConstants.CORRELATION_ID_HEADER))
                .filter(header -> !header.isBlank())
                .or(() -> resolveRequest()
                .map(request -> request.getHeader(AuthConstants.CORRELATION_ID_HEADER))
                .filter(header -> !header.isBlank()))
                .flatMap(this::parseUuid);
    }

    private String resolveIpAddress(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private Optional<UUID> parseUuid(String value) {
        try {
            return Optional.of(UUID.fromString(value));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}
