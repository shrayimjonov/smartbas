package uz.davrbank.assignmentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.assignmentservice.aggregation.dto.general.TypeRequestDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.TypeResponseDTO;
import uz.davrbank.assignmentservice.aggregation.filter.TypeFilter;
import uz.davrbank.assignmentservice.service.dao.TypeService;
import uz.davrbank.assignmentservice.utils.ApiConstants;

import java.util.List;

@Tag(name = "Типы", description = "Типы экзаменов")
@RestController
@RequestMapping(ApiConstants.typeRootApi)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TypeController {

    TypeService typeService;

    @Operation(summary = "Создание типа")
    @PostMapping
    public ResponseEntity<ResponseSuccess> create(@RequestBody TypeRequestDTO typeRequestDTO) {
        return ResponseEntity.ok(typeService.create(typeRequestDTO));
    }

    @Operation(summary = "Обновление типа")
    @PutMapping(ApiConstants.id)
    public ResponseEntity<ResponseSuccess> update(@PathVariable Long id,
                                                  @RequestBody TypeRequestDTO typeRequestDTO) {
        return ResponseEntity.ok(typeService.update(id, typeRequestDTO));
    }

    @Operation(summary = "Получение всех типов")
    @GetMapping(ApiConstants.list)
    public ResponseEntity<List<TypeResponseDTO>> getList(@ParameterObject TypeFilter filter) {
        return ResponseEntity.ok(typeService.getList(filter));
    }

    @Operation(summary = "Получение всех типов с пагинация")
    @GetMapping
    public ResponseEntity<PageResponseDTO<TypeResponseDTO>> getPageable(@ParameterObject TypeFilter filter) {
        return ResponseEntity.ok(typeService.getPageable(filter));
    }

    @Operation(summary = "Получение типа по id")
    @GetMapping(ApiConstants.id)
    public ResponseEntity<TypeResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(typeService.get(id));
    }

    @Operation(summary = "Удаление типа")
    @DeleteMapping(ApiConstants.id)
    public ResponseEntity<ResponseSuccess> delete(@PathVariable Long id) {
        return ResponseEntity.ok(typeService.delete(id));
    }
}
