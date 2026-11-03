package uz.davrbank.partnervalidationservice.merchant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uz.davrbank.partnervalidationservice.merchant.enums.Currency;

import java.util.UUID;

public record MerchantAccountDTO(
        @NotNull UUID merchantId,
        @NotBlank String branchCode,
        @NotBlank String accountNumber,
        @NotNull Currency currency,
        @NotBlank String absClientId
) {
}
