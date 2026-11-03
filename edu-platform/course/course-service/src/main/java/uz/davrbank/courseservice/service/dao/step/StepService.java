package uz.davrbank.courseservice.service.dao.step;

import uz.davrbank.courseservice.aggregation.dto.step.FileUploadDTO;
import uz.davrbank.courseservice.aggregation.dto.step.StepDTO;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;

import java.util.List;

public interface StepService {
    ResponseSuccess create(StepDTO stepDTO);

    ResponseSuccess update(StepDTO stepDTO, Long id);

    List<StepDTO> getAllByCourseId(Long courseId);

    ResponseSuccess delete(Long id);

    StepDTO get(Long id);

    List<FileUploadDTO> getAllFiles(Long stepId);
}
