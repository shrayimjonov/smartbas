package uz.davrbank.mapper;


import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import uz.davrbank.dto.response.ArchiveResponseDto;
import uz.davrbank.entity.Archive;

@Mapper(componentModel = "spring")
public interface ArchiveMapper {

    ArchiveResponseDto toDto(Archive archive);

    @InheritInverseConfiguration
    Archive toEntity(ArchiveResponseDto dto);

}
