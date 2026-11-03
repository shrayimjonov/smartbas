package uz.davrbank.mapper;

import org.mapstruct.Mapper;
import uz.davrbank.dto.response.PhotoUploadResponseDto;
import uz.davrbank.entity.PhotoUpload;

@Mapper(componentModel = "spring")
public interface PhotoUploadMapper {
    PhotoUploadResponseDto toDto(PhotoUpload savedPhotoUpload);
}
