
package uz.davrbank.courseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.courseservice.aggregation.dto.course.CourseRequestDTO;
import uz.davrbank.courseservice.aggregation.dto.course.CourseResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.filter.CourseFilter;
import uz.davrbank.courseservice.service.dao.course.CourseService;
import uz.davrbank.courseservice.utils.ApiConstants;

import java.util.List;
import java.util.Set;


@Tag(name = "Контроллер курсов", description = "Контроллер для работы с курсами")
@RestController
@RequestMapping(ApiConstants.courseRootApi)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CourseController {

    CourseService courseService;

    @Operation(summary = "Создание курса")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseRequestDTO request,
                                    @RequestParam("userId") String userId) {
        return ResponseEntity.ok(courseService.create(request, userId));
    }

    @Operation(summary = "Обновление курса")
    @PutMapping(ApiConstants.id)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CourseRequestDTO request) {
        return ResponseEntity.ok(courseService.update(id, request));
    }

    @Operation(summary = "Получение всех курсов с пагинация")
    @GetMapping
    public ResponseEntity<PageResponseDTO<CourseResponseDTO>> getPageable(@ParameterObject CourseFilter filter) {
        return ResponseEntity.ok(courseService.getPageable(filter));
    }

    @Operation(summary = "Получение всех курсов")
    @GetMapping(ApiConstants.list)
    public ResponseEntity<List<CourseResponseDTO>> getList(@ParameterObject CourseFilter filter) {
        return ResponseEntity.ok(courseService.getList(filter));
    }

    @Operation(summary = "Удаление курса")
    @DeleteMapping(ApiConstants.id)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.delete(id));
    }

    @Operation(summary = "Получение курса по ID")
    @GetMapping(ApiConstants.id)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.get(id));
    }

    @Operation(summary = "Предоставление доступа к курсу")
    @PostMapping(ApiConstants.access)
    public ResponseEntity<ResponseSuccess> accessEmployee(@PathVariable("id") Long courseId,
                                                          @RequestBody Set<String> employeeIds) {
        courseService.accessEmployees(courseId, employeeIds);
        return ResponseEntity.ok(new ResponseSuccess("Successfully!"));
    }
}
