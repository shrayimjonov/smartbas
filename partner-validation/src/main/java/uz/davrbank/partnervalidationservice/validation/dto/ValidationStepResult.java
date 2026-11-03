package uz.davrbank.partnervalidationservice.validation.dto;

import uz.davrbank.partnervalidationservice.logging.enums.MonitoringStep;

public record ValidationStepResult(
        MonitoringStep step,
        boolean success,
        String message
) {
}
