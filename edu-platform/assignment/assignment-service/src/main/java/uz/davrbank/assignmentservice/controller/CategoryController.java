package uz.davrbank.assignmentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.assignmentservice.aggregation.dto.course.CategoryRequestDTO;
import uz.davrbank.assignmentservice.aggregation.dto.course.CategoryResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.filter.CategoryFilter;
import uz.davrbank.assignmentservice.service.dao.CategoryService;
import uz.davrbank.assignmentservice.utils.ApiConstants;

import java.util.List;


@Tag(name = "Управление категориями", description = "Управление категориями")
@RestController
@RequestMapping(ApiConstants.categoryRootApi)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CategoryController {
    CategoryService categoryService;

    @Operation(summary = "Создание категории")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(categoryService.create(categoryRequestDTO));
    }

    @Operation(summary = "Обновление категории")
    @PutMapping(ApiConstants.id)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(categoryService.update(id, categoryRequestDTO));
    }

    @Operation(summary = "Получение категории по ID")
    @GetMapping(ApiConstants.id)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @Operation(summary = "Удаление категории")
    @DeleteMapping(ApiConstants.id)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }

    @Operation(summary = "Получение всех категорий")
    @GetMapping(ApiConstants.list)
    public ResponseEntity<List<CategoryResponseDTO>> getList(@ParameterObject CategoryFilter filter) {
        return ResponseEntity.ok(categoryService.getList(filter));
    }

    @Operation(summary = "Получение всех категорий с пагинация")
    @GetMapping
    public ResponseEntity<PageResponseDTO<CategoryResponseDTO>> getPageable(@ParameterObject CategoryFilter filter) {
        return ResponseEntity.ok(categoryService.getPageable(filter));
    }
}

