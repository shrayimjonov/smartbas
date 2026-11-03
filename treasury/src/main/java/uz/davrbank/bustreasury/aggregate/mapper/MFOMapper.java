package uz.davrbank.bustreasury.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import uz.davrbank.bustreasury.aggregate.dto.request.MFORequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.MFOResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.MFO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MFOMapper {

    MFOMapper INSTANCE = Mappers.getMapper(MFOMapper.class);

    MFOResponseDTO mfoToDTO(MFO mfo);

    MFO mfoDTOToMFO(MFORequestDTO mfoDTO);

    void updateMFOFromDTO(MFORequestDTO mfoDTO, @MappingTarget MFO mfo);
}
