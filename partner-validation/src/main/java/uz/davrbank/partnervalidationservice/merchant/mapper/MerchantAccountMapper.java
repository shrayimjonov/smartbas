package uz.davrbank.partnervalidationservice.merchant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantAccountDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantAccountDetailDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantAccountListDTO;
import uz.davrbank.partnervalidationservice.merchant.entity.MerchantAccount;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantAccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "merchantId", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MerchantAccount toEntity(MerchantAccountDTO request);

    MerchantAccountListDTO toListDTO(MerchantAccount account);

    @Mapping(target = "merchantId", source = "merchantId")
    MerchantAccountDetailDTO toDetailDTO(MerchantAccount account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "merchantId", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(MerchantAccountDTO request, @MappingTarget MerchantAccount account);
}
