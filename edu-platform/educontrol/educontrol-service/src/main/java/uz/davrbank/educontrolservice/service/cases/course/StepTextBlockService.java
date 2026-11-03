package uz.davrbank.educontrolservice.service.cases.course;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.step.StepTextBlockRequestDTO;
import uz.davrbank.educontrolservice.feign.course.StepTextBlockFeign;
import uz.davrbank.educontrolservice.service.dao.course.StepTextBlockInterface;

import java.util.List;

@Service
@Slf4j
public class StepTextBlockService implements StepTextBlockInterface {
    private final StepTextBlockFeign stepTextBlockFeign;

    public StepTextBlockService(StepTextBlockFeign stepTextBlockFeign) {
        this.stepTextBlockFeign = stepTextBlockFeign;
    }

    @Override
    public Object createStepTextBlock(StepTextBlockRequestDTO requestDTO) {
        log.info("REQUEST (createStepTextBlock) FROM [course-service]");
        return stepTextBlockFeign.createStepTextBlock(requestDTO);
    }

    @Override
    public Object updateSteTextBlock(Long blockId, StepTextBlockRequestDTO requestDTO) {
        log.info("REQUEST (updateSteTextBlock) FROM [course-service]");
        return stepTextBlockFeign.updateStepTextBlock(blockId, requestDTO);
    }

    @Override
    public Object deleteStepTextBlock(Long id) {
        log.info("REQUEST (deleteStepTextBlock) FROM [course-service]");
        return stepTextBlockFeign.deleteStepTextBlock(id);
    }

    @Override
    public Object getStepTextBlock(Long id) {
        log.info("REQUEST (getStepTextBlock) FROM [course-service]");
        return stepTextBlockFeign.getStepTextBlockById(id);
    }

    @Override
    public List<Object> getAllStepTextBlock(Long stepId) {
        log.info("REQUEST (getAllStepTextBlock) FROM [course-service]");
        return stepTextBlockFeign.getAllStepTextBlocks(stepId);
    }
}
