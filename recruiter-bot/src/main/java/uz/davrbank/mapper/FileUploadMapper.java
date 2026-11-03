package uz.davrbank.mapper;

import org.mapstruct.Mapper;
import uz.davrbank.dto.response.FileUploadResponse;
import uz.davrbank.entity.FileUpload;

@Mapper(componentModel = "spring")
public interface FileUploadMapper {
    FileUploadResponse toDto(FileUpload fileUpload);
}
