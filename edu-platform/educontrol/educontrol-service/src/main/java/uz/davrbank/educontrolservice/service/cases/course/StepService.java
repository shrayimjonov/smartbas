package uz.davrbank.educontrolservice.service.cases.course;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.step.StepDTO;
import uz.davrbank.educontrolservice.feign.course.StepFeign;
import uz.davrbank.educontrolservice.service.dao.course.StepInterface;

import java.util.List;

@Service
@Slf4j
public class StepService implements StepInterface {
    private final StepFeign stepFeign;

    public StepService(StepFeign stepFeign) {
        this.stepFeign = stepFeign;
    }

    @Override
    public Object createStep(StepDTO stepDTO) {
        log.info("REQUEST (createStep) FROM [course-service]");
        return stepFeign.createStep(stepDTO);
    }

    @Override
    public Object updateStep(StepDTO stepDTO, Long id) {
        log.info("REQUEST (updateStep) FROM [course-service]");
        return stepFeign.updateStep(id, stepDTO);
    }

    @Override
    public List<Object> getStepsByCourseId(Long courseId) {
        log.info("REQUEST (getStepsByCourseId) FROM [course-service]");
        return stepFeign.getAllCoursesStep(courseId);
    }

    @Override
    public Object deleteStep(Long id) {
        log.info("REQUEST (deleteStep) FROM [course-service]");
        return stepFeign.deleteStep(id);
    }

    @Override
    public Object getStepById(Long id) {
        log.info("REQUEST (getStepById) FROM [course-service]");
        return stepFeign.getStepById(id);
    }

    @Override
    public List<Object> getAllFilesByStepId(Long stepId) {
        log.info("REQUEST (getAllFilesByStepId) FROM [course-service]");
        return stepFeign.getAllFilesInfo(stepId);
    }
}
