package uz.davrbank.partnervalidationservice.merchant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashSet;

public record MerchantDTO(
        @NotBlank String merchantExternalId,
        @NotBlank String name,
        @NotBlank String tin,
        @NotNull LinkedHashSet<String> allowedOperations
) {
}
