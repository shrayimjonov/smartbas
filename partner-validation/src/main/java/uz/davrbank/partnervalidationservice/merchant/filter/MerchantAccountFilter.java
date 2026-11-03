package uz.davrbank.partnervalidationservice.merchant.filter;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.partnervalidationservice.common.filter.BaseFilter;
import uz.davrbank.partnervalidationservice.merchant.enums.AccountStatus;
import uz.davrbank.partnervalidationservice.merchant.enums.Currency;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantAccountFilter extends BaseFilter {

    UUID merchantId;

    AccountStatus status;

    Currency currency;
}
