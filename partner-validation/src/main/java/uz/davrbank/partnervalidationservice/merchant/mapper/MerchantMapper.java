package uz.davrbank.partnervalidationservice.merchant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantDetailDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantListDTO;
import uz.davrbank.partnervalidationservice.merchant.entity.Merchant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MerchantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Merchant toEntity(MerchantDTO request);

    MerchantDetailDTO toDetailDTO(Merchant merchant);

    MerchantListDTO toListDTO(Merchant merchant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(MerchantDTO request, @MappingTarget Merchant merchant);
}
