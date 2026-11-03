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
import uz.davrbank.educontrolservice.aggregate.dto.assigment.typecategory.TypeDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.TypeFilter;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.typecategory.TypeService;


@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DAVR', 'ROLE_HR')")
@RestController
@RequestMapping("/api/v1/educontrol/assignment-service/type")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TypeController {

    TypeService typeService;


    @PostMapping
    public ResponseEntity<ApiResponse<Object>> crate(@RequestBody @Valid TypeDTO typeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Тип успешно создан",
                        typeService.create(typeDTO)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getPageable(@ParameterObject TypeFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список типов получен",
                typeService.getPageable(filter)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getList(@ParameterObject TypeFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список типов получен",
                typeService.getList(filter)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Тип успешно удалён",
                typeService.delete(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> update(@PathVariable Long id,
                                                             @RequestBody TypeDTO typeDTO) {
        return ResponseEntity.ok(ApiResponse.of("Тип успешно обновлён",
                typeService.update(id, typeDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о типе получена",
                typeService.get(id)));
    }
}