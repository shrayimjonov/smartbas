package uz.davrbank.bustreasury.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import uz.davrbank.bustreasury.aggregate.dto.request.WiringRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.Wiring;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WiringMapper {

    WiringMapper INSTANCE = Mappers.getMapper(WiringMapper.class);

    WiringResponseDTO wiringToWiringDTO(Wiring wiring);

    Wiring wiringDTOToWiring(WiringRequestDTO wiringDTO);

    void updateWiringFromDTO(WiringRequestDTO wiringDTO, @MappingTarget Wiring wiring);
}
