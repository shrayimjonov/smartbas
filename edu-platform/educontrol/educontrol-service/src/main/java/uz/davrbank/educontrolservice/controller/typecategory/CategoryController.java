package uz.davrbank.educontrolservice.controller.typecategory;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.typecategory.CategoryDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CategoryFilter;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.typecategory.CategoryService;


@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DAVR', 'ROLE_HR')")
@RestController
@RequestMapping("/api/v1/educontrol/assignment-service/category")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody @Valid CategoryDTO categoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Категория успешно создана",
                        categoryService.create(categoryDTO)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getPageable(@ParameterObject CategoryFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список категорий получен",
                categoryService.getPageable(filter)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getList(@ParameterObject CategoryFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список категорий получен",
                categoryService.getList(filter)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Категория успешно удалена",
                categoryService.delete(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(ApiResponse.of("Категория успешно обновлена",
                categoryService.update(id, categoryDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о категории получена",
                categoryService.get(id)));
    }
}