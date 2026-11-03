package uz.davrbank.bustreasury.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import uz.davrbank.bustreasury.aggregate.dto.request.DocTypeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.DocTypeResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.DocType;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DocTypeMapper {

    DocTypeMapper INSTANCE = Mappers.getMapper(DocTypeMapper.class);

    DocTypeResponseDTO wiringToDocTypeDTO(DocType wiring);

    DocType wiringDTOToDocType(DocTypeRequestDTO wiringDTO);

    void updateDocTypeFromDTO(DocTypeRequestDTO wiringDTO, @MappingTarget DocType wiring);
}
