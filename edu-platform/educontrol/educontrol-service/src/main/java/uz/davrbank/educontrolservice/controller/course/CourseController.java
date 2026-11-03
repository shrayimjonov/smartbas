package uz.davrbank.educontrolservice.controller.course;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.course.CourseRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CourseFilter;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.course.CourseService;

import java.util.Set;


@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DAVR', 'ROLE_HR')")
@RestController
@RequestMapping("/api/v1/educontrol/course-service/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody @Valid CourseRequestDTO courseRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Курс успешно создан",
                        courseService.create(courseRequestDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> update(@PathVariable Long id, @RequestBody CourseRequestDTO courseRequestDTO) {
        return ResponseEntity.ok(ApiResponse.of("Курс успешно обновлён",
                courseService.update(id, courseRequestDTO)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getPageable(@ParameterObject CourseFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список курсов получен",
                courseService.getPageable(filter)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getList(@ParameterObject CourseFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список курсов получен",
                courseService.getList(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о курсе получена",
                courseService.get(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Курс успешно удалён",
                courseService.delete(id)));
    }

    @PostMapping("/{courseId}/access")
    public ResponseEntity<ApiResponse<Object>> accessEmployee(@PathVariable Long courseId,
                                                              @RequestBody Set<String> employeeIds) {
        return ResponseEntity.ok(ApiResponse.of("Доступ к курсу предоставлен",
                courseService.accessEmployees(courseId, employeeIds)));
    }

}
