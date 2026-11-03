package uz.davrbank.partnervalidationservice.merchant.filter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.partnervalidationservice.common.filter.BaseFilter;
import uz.davrbank.partnervalidationservice.merchant.enums.MerchantStatus;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantFilter extends BaseFilter {

    MerchantStatus status;

    String merchantExternalId;
}
