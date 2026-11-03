package uz.davrbank.educontrolservice.controller.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.step.StepDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.course.StepInterface;

@RestController
@RequestMapping("/api/v1/educontrol/course-service/step")
public class StepController {
    private final StepInterface stepInterface;

    public StepController(StepInterface stepInterface) {
        this.stepInterface = stepInterface;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createStep(@RequestBody StepDTO stepDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Шаг успешно создан",
                        stepInterface.createStep(stepDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateStep(@PathVariable Long id, @RequestBody StepDTO stepDTO) {
        return ResponseEntity.ok(ApiResponse.of("Шаг успешно обновлён",
                stepInterface.updateStep(stepDTO, id)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getAllCoursesStep(@RequestParam("courseId") Long courseId) {
        return ResponseEntity.ok(ApiResponse.of("Список шагов курса получен",
                stepInterface.getStepsByCourseId(courseId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteStep(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Шаг успешно удалён",
                stepInterface.deleteStep(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getStepById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о шаге получена",
                stepInterface.getStepById(id)));
    }

    @GetMapping("/files")
    public ResponseEntity<ApiResponse<Object>> getAllFilesByStepId(@RequestParam("stepId") Long stepId) {
        return ResponseEntity.ok(ApiResponse.of("Список файлов шага получен",
                stepInterface.getAllFilesByStepId(stepId)));
    }
}
