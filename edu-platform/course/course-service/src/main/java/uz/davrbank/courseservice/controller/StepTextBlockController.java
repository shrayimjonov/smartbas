package uz.davrbank.courseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.courseservice.aggregation.dto.step.StepTextBlockRequestDTO;
import uz.davrbank.courseservice.service.dao.step.StepTextBlockService;
import uz.davrbank.courseservice.utils.ApiConstants;

@Tag(name = "Контентные блоки", description = "Контентные блоки для шагов")
@RestController
@RequestMapping(ApiConstants.stepTextBlockRootApi)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StepTextBlockController {
    StepTextBlockService stepTextBlockService;

    @Operation(summary = "Создание контентного блока")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody StepTextBlockRequestDTO requestDTO) {
        return ResponseEntity.ok(stepTextBlockService.create(requestDTO));
    }

    @Operation(summary = "Обновление контентного блока")
    @PutMapping(ApiConstants.id)
    public ResponseEntity<?> update(@PathVariable("id") Long blockId,
                                    @RequestBody StepTextBlockRequestDTO requestDTO) {
        return ResponseEntity.ok(stepTextBlockService.update(blockId, requestDTO));
    }

    @Operation(summary = "Удаление контентного блока")
    @DeleteMapping(ApiConstants.id)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(stepTextBlockService.delete(id));
    }

    @Operation(summary = "Получение всех контентных блоков")
    @GetMapping(ApiConstants.list)
    public ResponseEntity<?> getList(@RequestParam("stepId") Long stepId) {
        return ResponseEntity.ok(stepTextBlockService.getList(stepId));
    }

    @Operation(summary = "Получение контентного блока по ID")
    @GetMapping(ApiConstants.id)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(stepTextBlockService.get(id));
    }


}
