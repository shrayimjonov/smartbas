package uz.davrbank.educontrolservice.service.cases.course;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.filter.StepEmployeeFilter;
import uz.davrbank.educontrolservice.feign.course.StepEmployeeFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.course.StepEmployeeInterface;

import java.util.List;

@Service
@Slf4j
public class StepEmployeeService implements StepEmployeeInterface {

    private final StepEmployeeFeign stepEmployeeFeign;
    private final JwtTokenService jwtTokenService;

    public StepEmployeeService(StepEmployeeFeign stepEmployeeFeign, JwtTokenService jwtTokenService) {
        this.stepEmployeeFeign = stepEmployeeFeign;
        this.jwtTokenService = jwtTokenService;
    }


    @Override
    public Object startedStep(Long stepId) {
        log.info("REQUEST (startedStep) FROM [course-service]");
        String employeeId = jwtTokenService.getCurrentUserId();

        return stepEmployeeFeign.startedStep(stepId, employeeId);
    }

    @Override
    public Object completedStep(Long stepId) {
        log.info("REQUEST (completedStep) FROM [course-service]");
        String employeeId = jwtTokenService.getCurrentUserId();

        return stepEmployeeFeign.completedStep(stepId, employeeId);
    }

    @Override
    public Object getPageable(StepEmployeeFilter filter) {
        log.info("REQUEST (getPageable) FROM [course-service]");
        StepEmployeeFilter prepared = filter != null ? filter : new StepEmployeeFilter();
        return stepEmployeeFeign.getPageable(prepared);
    }

    @Override
    public Object getList(StepEmployeeFilter filter) {
        log.info("REQUEST (getList) FROM [course-service]");
        StepEmployeeFilter prepared = filter != null ? filter : new StepEmployeeFilter();
        return stepEmployeeFeign.getList(prepared);
    }

    @Override
    public List<Object> getEmployeeSteps() {
        log.info("REQUEST (getEmployeeSteps) FROM [course-service]");
        String employeeId = jwtTokenService.getCurrentUserId();
        return stepEmployeeFeign.employeeStep(employeeId);
    }
}
