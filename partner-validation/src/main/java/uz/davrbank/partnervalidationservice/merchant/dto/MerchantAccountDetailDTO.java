package uz.davrbank.partnervalidationservice.merchant.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.partnervalidationservice.merchant.enums.AccountStatus;
import uz.davrbank.partnervalidationservice.merchant.enums.Currency;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantAccountDetailDTO {

    UUID id;
    UUID merchantId;
    String branchCode;
    String accountNumber;
    Currency currency;
    String absClientId;
    AccountStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    MerchantDetailDTO merchant;
}
