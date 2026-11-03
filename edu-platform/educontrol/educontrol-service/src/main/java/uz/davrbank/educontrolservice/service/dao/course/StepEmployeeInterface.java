package uz.davrbank.educontrolservice.service.dao.course;

import uz.davrbank.educontrolservice.aggregate.dto.filter.StepEmployeeFilter;

import java.util.List;

public interface StepEmployeeInterface {

    Object startedStep(Long stepId);

    Object completedStep(Long stepId);

    Object getPageable(StepEmployeeFilter filter);

    Object getList(StepEmployeeFilter filter);

    List<Object> getEmployeeSteps();
}
