package uz.davrbank.partnervalidationservice.merchant.dto;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.partnervalidationservice.merchant.enums.AccountStatus;
import uz.davrbank.partnervalidationservice.merchant.enums.Currency;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantAccountListDTO {

    UUID id;
    String branchCode;
    String accountNumber;
    Currency currency;
    String absClientId;
    AccountStatus status;
    MerchantListDTO merchant;
}
