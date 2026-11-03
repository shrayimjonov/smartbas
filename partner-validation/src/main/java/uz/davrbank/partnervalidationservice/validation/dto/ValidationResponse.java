package uz.davrbank.partnervalidationservice.validation.dto;

public record ValidationResponse(
        boolean approved,
        Object validatedPayload,
        String errorCode,
        String errorMessage
) {
}