package uz.davrbank.courseservice.service.cases.step;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.dto.step.StepTextBlockRequestDTO;
import uz.davrbank.courseservice.aggregation.dto.step.StepTextBlockResponseDTO;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.mapper.StepTextBlockMapper;
import uz.davrbank.courseservice.aggregation.model.step.Step;
import uz.davrbank.courseservice.aggregation.model.step.StepTextBlock;
import uz.davrbank.courseservice.repository.StepRepository;
import uz.davrbank.courseservice.repository.StepTextBlockRepository;
import uz.davrbank.courseservice.service.dao.step.StepTextBlockService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StepTextBlockServiceImpl implements StepTextBlockService {
    private final static Logger log = LoggerFactory.getLogger(StepTextBlockServiceImpl.class);
    StepTextBlockRepository stepTextBlockRepository;
    StepTextBlockMapper stepTextBlockMapper;
    StepRepository stepRepository;

    @Override
    public ResponseSuccess create(StepTextBlockRequestDTO dto) {
        log.info("Запрос (createStepTextBlock) от [step-text-block-service]");

        try {
            Step step = stepRepository.findById(dto.stepId())
                    .orElseThrow(() -> new DataNotFoundException("Step not found"));

            StepTextBlock block = stepTextBlockMapper.toEntityFromReq(dto);
            block.setStep(step);

            // Генерация orderInStep:
            Integer maxOrder = stepTextBlockRepository.findMaxOrderInStep(step.getId());
            block.setOrderInStep((maxOrder == null ? 0 : maxOrder) + 1);

            stepTextBlockRepository.save(block);

            return new ResponseSuccess("Step text block created successfully");
        } catch (Exception e) {
            log.error("Error creating step text block: {}", e.getMessage());
            throw new RuntimeException("Error creating step text block: " + e.getMessage());
        }
    }


    @Override
    public ResponseSuccess update(Long blockId, StepTextBlockRequestDTO stepTextBlockRequestDTO) {
        log.info("Запрос (updateStepTextBlock) от [step-text-block-service]");
        try {
            StepTextBlock stepTextBlock = stepTextBlockRepository.findById(blockId)
                    .orElseThrow(() -> new DataNotFoundException("Step text block not found"));
            stepTextBlockMapper.updateFromReqDto(stepTextBlockRequestDTO, stepTextBlock);
            stepTextBlockRepository.save(stepTextBlock);
            return new ResponseSuccess(
                    "Step text block updated successfully"
            );
        } catch (Exception e) {
            log.info("Error updating step text block: {}", e.getMessage());
            throw new RuntimeException("Error updating step text block" + e.getMessage());
        }
    }

    @Override
    public ResponseSuccess delete(Long stepTextBlockId) {
        log.info("Запрос (deleteStepTextBlock) от [step-text-block-service]");
        try {
            StepTextBlock stepTextBlock = stepTextBlockRepository.findById(stepTextBlockId)
                    .orElseThrow(() -> new DataNotFoundException("Step text block not found"));
            stepTextBlockRepository.delete(stepTextBlock);
            return new ResponseSuccess(
                    "Step text block deleted successfully"
            );
        } catch (Exception e) {
            log.info("Error deleting step text block: {}", e.getMessage());
            throw new RuntimeException("Error deleting step text block" + e.getMessage());
        }
    }

    @Override
    public StepTextBlockResponseDTO get(Long stepTextBlockId) {
        log.info("Запрос (getStepTextBlock) от [step-text-block-service]");
        try {
            StepTextBlock stepTextBlock = stepTextBlockRepository.findById(stepTextBlockId)
                    .orElseThrow(() -> new DataNotFoundException("Step text block not found"));
            return stepTextBlockMapper.toDto(stepTextBlock);
        } catch (Exception e) {
            log.info("Error getting step text block: {}", e.getMessage());
            throw new RuntimeException("Error getting step text block" + e.getMessage());
        }
    }

    @Override
    public List<StepTextBlockResponseDTO> getList(Long stepId) {
        log.info("Запрос (getAllStepTextBlockByStepId) от [step-text-block-service]");
        try {
            List<StepTextBlock> stepTextBlocks = stepTextBlockRepository.findAllByStepId(stepId);
            return stepTextBlockMapper.toDtoList(stepTextBlocks);
        } catch (Exception e) {
            log.info("Error getting all step text blocks by step ID: {}", e.getMessage());
            throw new RuntimeException("Error getting all step text blocks by step ID" + e.getMessage());
        }
    }
}
