package uz.davrbank.educontrolservice.service.dao.course;

import uz.davrbank.educontrolservice.aggregate.dto.assigment.step.StepTextBlockRequestDTO;

import java.util.List;

public interface StepTextBlockInterface {

    Object createStepTextBlock(StepTextBlockRequestDTO requestDTO);

    Object updateSteTextBlock(Long blockId, StepTextBlockRequestDTO requestDTO);

    Object deleteStepTextBlock(Long id);

    Object getStepTextBlock(Long id);

    List<Object> getAllStepTextBlock(Long stepId);
}
