package uz.davrbank.educontrolservice.controller.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.step.StepTextBlockRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.course.StepTextBlockInterface;

@RestController
@RequestMapping("/api/v1/educontrol/course-service/step/text-blocks")
public class StepTextBlockController {
    private final StepTextBlockInterface stepTextBlockInterface;

    public StepTextBlockController(StepTextBlockInterface stepTextBlockInterface) {
        this.stepTextBlockInterface = stepTextBlockInterface;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createStepTextBlock(@RequestBody StepTextBlockRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Текстовый блок успешно создан",
                        stepTextBlockInterface.createStepTextBlock(requestDTO)));
    }

    @PutMapping("/{blockId}")
    public ResponseEntity<ApiResponse<Object>> updateStepTextBlock(@PathVariable Long blockId,
                                                                   @RequestBody StepTextBlockRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.of("Текстовый блок успешно обновлён",
                stepTextBlockInterface.updateSteTextBlock(blockId, requestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteStepTextBlock(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Текстовый блок успешно удалён",
                stepTextBlockInterface.deleteStepTextBlock(id)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getAllStepTextBlock(@RequestParam("stepId") Long stepId) {
        return ResponseEntity.ok(ApiResponse.of("Список текстовых блоков получен",
                stepTextBlockInterface.getAllStepTextBlock(stepId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getStepTextBlock(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о текстовом блоке получена",
                stepTextBlockInterface.getStepTextBlock(id)));
    }
}
