package uz.davrbank.partnervalidationservice.validation.dto;

public record BeneficiaryValidationPayload(
        String beneficiaryClientId,
        String beneficiaryAccountId,
        String beneficiaryBranch
) {
}
