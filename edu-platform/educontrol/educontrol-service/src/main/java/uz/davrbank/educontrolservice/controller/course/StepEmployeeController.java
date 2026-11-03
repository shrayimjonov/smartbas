package uz.davrbank.educontrolservice.controller.course;


import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.filter.StepEmployeeFilter;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.course.StepEmployeeInterface;

@RestController
@RequestMapping("/api/v1/educontrol/course-service/step/employee")
public class StepEmployeeController {

    private final StepEmployeeInterface stepEmployeeInterface;

    public StepEmployeeController(StepEmployeeInterface stepEmployeeInterface) {
        this.stepEmployeeInterface = stepEmployeeInterface;
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<Object>> startStep(@RequestParam Long stepId) {
        return ResponseEntity.ok(ApiResponse.of("Шаг начат",
                stepEmployeeInterface.startedStep(stepId)));
    }

    @PostMapping("/complete")
    public ResponseEntity<ApiResponse<Object>> completeStep(@RequestParam Long stepId) {
        return ResponseEntity.ok(ApiResponse.of("Шаг завершён",
                stepEmployeeInterface.completedStep(stepId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getPageable(@ParameterObject StepEmployeeFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список прикреплённых шагов получен",
                stepEmployeeInterface.getPageable(filter)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getList(@ParameterObject StepEmployeeFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список прикреплённых шагов получен",
                stepEmployeeInterface.getList(filter)));
    }

    @GetMapping("/attached")
    public ResponseEntity<ApiResponse<Object>> employeeCourse() {
        return ResponseEntity.ok(ApiResponse.of("Список шагов сотрудника получен",
                stepEmployeeInterface.getEmployeeSteps()));
    }
}
