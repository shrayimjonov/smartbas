package uz.davrbank.courseservice.aggregation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.davrbank.courseservice.aggregation.dto.step.FileUploadDTO;
import uz.davrbank.courseservice.aggregation.model.FileUpload;

import java.util.List;


@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(target = "stepId", expression = "java(fileUpload.getResourceId())")
    List<FileUploadDTO> toDtoList(List<FileUpload> fileUpload);

    @Mapping(target = "stepId", expression = "java(fileUpload.getResourceId())")
    FileUploadDTO toDto(FileUpload fileUpload);
}
