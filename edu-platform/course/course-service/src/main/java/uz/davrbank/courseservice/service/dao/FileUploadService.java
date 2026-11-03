package uz.davrbank.courseservice.service.dao;

import uz.davrbank.courseservice.aggregation.dto.step.FileUploadDTO;
import uz.davrbank.courseservice.aggregation.enums.ResourceType;

import java.util.List;

public interface FileUploadService {

    List<FileUploadDTO> getAllFilesInfo(Long stepId, ResourceType step);

    void deleteFileByResourceId(Long id, ResourceType step);
}
