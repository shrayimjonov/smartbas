package uz.davrbank.courseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.dto.step.AttachStepResponseDTO;
import uz.davrbank.courseservice.aggregation.filter.StepEmployeeFilter;
import uz.davrbank.courseservice.service.dao.step.StepEmployeeService;
import uz.davrbank.courseservice.utils.ApiConstants;

import java.util.List;

@Tag(name = "Управление шагами курса по сотрудникам")
@RestController
@RequestMapping(ApiConstants.stepEmployeeRootApi)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StepEmployeeController {


    StepEmployeeService stepEmployeeService;

    @Operation(summary = "Начать шаг курса")
    @PostMapping(ApiConstants.start)
    public ResponseEntity<ResponseSuccess> startCourse(@RequestParam("stepId") Long stepId,
                                                       @RequestParam("employeeId") String employeeId) {
        stepEmployeeService.startedStep(stepId, employeeId);
        return ResponseEntity.ok(new ResponseSuccess("The step has started."));
    }

    @Operation(summary = "Завершить шаг курса")
    @PostMapping(ApiConstants.complete)
    public ResponseEntity<ResponseSuccess> completeCourse(@RequestParam("stepId") Long stepId,
                                                          @RequestParam("employeeId") String employeeId) {
        stepEmployeeService.completedStep(stepId, employeeId);
        return ResponseEntity.ok(new ResponseSuccess("The step has completed."));
    }


    @Operation(summary = "Получить все шаги курса")
    @GetMapping
    public ResponseEntity<PageResponseDTO<AttachStepResponseDTO>> getPageable(@ParameterObject StepEmployeeFilter filter) {
        return ResponseEntity.ok(stepEmployeeService.getPageable(filter));
    }

    @Operation(summary = "Получить все шаги курса")
    @GetMapping(ApiConstants.list)
    public ResponseEntity<List<AttachStepResponseDTO>> getList(@ParameterObject StepEmployeeFilter filter) {
        return ResponseEntity.ok(stepEmployeeService.getList(filter));
    }

    @Deprecated
    @Operation(summary = "Получить шаги курса по id сотрудника")
    @GetMapping("/attached")
    public ResponseEntity<List<AttachStepResponseDTO>> employeeCourse(
            @RequestParam(value = "employeeId") String employeeId) {
        StepEmployeeFilter filter = new StepEmployeeFilter();
        filter.setEmployeeId(employeeId);
        return ResponseEntity.ok(stepEmployeeService.getList(filter));
    }
}
