package uz.davrbank.bustreasury.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import uz.davrbank.bustreasury.aggregate.dto.request.PurposeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.PurposeResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.Purpose;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PurposeMapper {

    PurposeMapper INSTANCE = Mappers.getMapper(PurposeMapper.class);

    PurposeResponseDTO purposeToPurposeDTO(Purpose purpose);

    Purpose purposeDTOToPurpose(PurposeRequestDTO purposeDTO);

    void updatePurposeFromDTO(PurposeRequestDTO purposeDTO, @MappingTarget Purpose purpose);
}