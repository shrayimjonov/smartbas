package uz.davrbank.courseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.courseservice.aggregation.dto.step.FileUploadDTO;
import uz.davrbank.courseservice.aggregation.dto.step.StepDTO;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.service.dao.step.StepService;
import uz.davrbank.courseservice.utils.ApiConstants;

import java.util.List;

@Tag(name = "Шаги", description = "Шаги")
@RestController
@RequestMapping(ApiConstants.stepRootApi)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StepController {

    StepService stepService;

    @Operation(summary = "Создание шага")
    @PostMapping
    public ResponseEntity<ResponseSuccess> create(@RequestBody StepDTO stepDTO) {
        return ResponseEntity.ok(stepService.create(stepDTO));
    }

    @Operation(summary = "Обновление шага")
    @PutMapping(ApiConstants.id)
    public ResponseEntity<ResponseSuccess> update(
            @PathVariable Long id, @RequestBody StepDTO stepDTO) {
        return ResponseEntity.ok(stepService.update(stepDTO, id));
    }

    @Operation(summary = "Получение всех шагов по курсу")
    @GetMapping(ApiConstants.list)
    public ResponseEntity<?> getAllByCourseId(@RequestParam("courseId") Long courseId) {
        List<StepDTO> pageResponseDTO = stepService
                .getAllByCourseId(courseId);
        return ResponseEntity.ok(pageResponseDTO);
    }

    @Operation(summary = "Удаление шага")
    @DeleteMapping(ApiConstants.id)
    public ResponseEntity<ResponseSuccess> delete(@PathVariable Long id) {
        return ResponseEntity.ok(stepService.delete(id));
    }


    @GetMapping(ApiConstants.id)
    public ResponseEntity<StepDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(stepService.get(id));
    }

    @Operation(summary = "Получение всех файлов по шагу")
    @GetMapping(ApiConstants.files)
    public ResponseEntity<List<FileUploadDTO>> getAllFiles(@RequestParam("stepId") Long stepId){
        return ResponseEntity.ok(stepService.getAllFiles(stepId));
    }
}
