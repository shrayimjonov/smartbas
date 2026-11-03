package uz.davrbank.courseservice.service.dao.step;

import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.dto.step.StepTextBlockRequestDTO;
import uz.davrbank.courseservice.aggregation.dto.step.StepTextBlockResponseDTO;

import java.util.List;

public interface StepTextBlockService {

    ResponseSuccess create(StepTextBlockRequestDTO stepTextBlockRequestDTO);

    ResponseSuccess update(Long blockId, StepTextBlockRequestDTO stepTextBlockRequestDTO);

    ResponseSuccess delete(Long stepTextBlockId);

    StepTextBlockResponseDTO get(Long stepTextBlockId);

    List<StepTextBlockResponseDTO> getList(Long stepId);

}
