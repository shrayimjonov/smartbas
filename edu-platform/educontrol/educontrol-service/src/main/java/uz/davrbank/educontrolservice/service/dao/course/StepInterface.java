package uz.davrbank.educontrolservice.service.dao.course;

import uz.davrbank.educontrolservice.aggregate.dto.assigment.step.StepDTO;

import java.util.List;

public interface StepInterface {

    Object createStep(StepDTO stepDTO);

    Object updateStep(StepDTO stepDTO, Long id);

    List<Object> getStepsByCourseId(Long courseId);

    Object deleteStep(Long id);

    Object getStepById(Long id);

    List<Object> getAllFilesByStepId(Long stepId);
}
