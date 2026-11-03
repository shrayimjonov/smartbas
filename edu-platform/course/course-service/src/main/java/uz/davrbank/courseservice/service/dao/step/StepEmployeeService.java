package uz.davrbank.courseservice.service.dao.step;

import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.step.AttachStepResponseDTO;
import uz.davrbank.courseservice.aggregation.filter.StepEmployeeFilter;

import java.util.List;

public interface StepEmployeeService {

    void startedStep(Long stepId, String employeeId);

    void completedStep(Long stepId, String employeeId);

    PageResponseDTO<AttachStepResponseDTO> getPageable(StepEmployeeFilter filter);

    List<AttachStepResponseDTO> getList(StepEmployeeFilter filter);
}
