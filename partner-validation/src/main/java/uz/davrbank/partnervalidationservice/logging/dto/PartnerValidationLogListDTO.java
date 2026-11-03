package uz.davrbank.partnervalidationservice.logging.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import uz.davrbank.partnervalidationservice.logging.enums.MonitoringStep;

public record PartnerValidationLogListDTO(
        UUID id,
        UUID correlationId,
        MonitoringStep monitoringStep,
        String request,
        String response,
        String methodType,
        String url,
        LocalDateTime requestDate,
        LocalDateTime responseDate,
        String ipAddress,
        boolean success
) {
}
