package uz.davrbank.partnervalidationservice.merchant.dto;

import java.util.LinkedHashSet;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.partnervalidationservice.merchant.enums.MerchantStatus;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantListDTO {

    UUID id;
    String merchantExternalId;
    String name;
    String tin;
    MerchantStatus status;
    LinkedHashSet<String> allowedOperations;
}
