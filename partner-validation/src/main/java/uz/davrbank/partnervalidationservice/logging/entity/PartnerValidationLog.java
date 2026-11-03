package uz.davrbank.partnervalidationservice.logging.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.partnervalidationservice.logging.enums.MonitoringStep;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "partner_validation_logs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerValidationLog {

    @Id
    @Column(nullable = false, updatable = false)
    UUID id;

    @Column(name = "correlation_id", nullable = false)
    UUID correlationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "monitoring_step", nullable = false, length = 64)
    MonitoringStep monitoringStep;

    @Column(name = "request", nullable = false, columnDefinition = "text")
    String request;

    @Column(name = "response", nullable = false, columnDefinition = "text")
    String response;

    @Column(name = "method_type", nullable = false, length = 16)
    String methodType;

    @Column(name = "request_url", nullable = false, length = 512)
    String url;

    @Column(name = "request_date", nullable = false)
    LocalDateTime requestDate;

    @Column(name = "response_date", nullable = false)
    LocalDateTime responseDate;

    @Column(name = "ip_address", nullable = false, length = 64)
    String ipAddress;

    @Column(name = "success", nullable = false)
    boolean success;
}
