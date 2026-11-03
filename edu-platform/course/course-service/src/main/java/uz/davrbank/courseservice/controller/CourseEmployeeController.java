package uz.davrbank.courseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.courseservice.aggregation.dto.course.UserCourseResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.filter.CourseEmployeeFilter;
import uz.davrbank.courseservice.service.dao.course.CourseEmployeeService;
import uz.davrbank.courseservice.utils.ApiConstants;

import java.util.List;

@Tag(name = "Управление курсами", description = "Управление курсами для сотрудников")
@RestController
@RequestMapping(ApiConstants.courseEmployeeRootApi)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CourseEmployeeController {

    CourseEmployeeService courseEmployeeService;

    @Operation(summary = "Начать курс")
    @PostMapping(ApiConstants.start)
    public ResponseEntity<ResponseSuccess> startCourse(@RequestParam("courseId") Long courseId,
                                                       @RequestParam("employeeId") String employeeId) {
        courseEmployeeService.startedCourse(courseId, employeeId);
        return ResponseEntity.ok(new ResponseSuccess("The course has started."));
    }

    @Operation(summary = "Завершить курс")
    @PostMapping(ApiConstants.complete)
    public ResponseEntity<ResponseSuccess> completeCourse(@RequestParam("courseId") Long courseId,
                                                          @RequestParam("employeeId") String employeeId) {
        courseEmployeeService.completedCourse(courseId, employeeId);
        return ResponseEntity.ok(new ResponseSuccess("The course has completed."));
    }

    @Operation(summary = "Все курсы с пагинация")
    @GetMapping
    public ResponseEntity<PageResponseDTO<UserCourseResponseDTO>> getPageable(@ParameterObject CourseEmployeeFilter filter) {
        return ResponseEntity.ok(courseEmployeeService.getPageable(filter));
    }

    @Operation(summary = "Все курсы")
    @GetMapping(ApiConstants.list)
    public ResponseEntity<List<UserCourseResponseDTO>> getList(@ParameterObject CourseEmployeeFilter filter) {
        return ResponseEntity.ok(courseEmployeeService.getList(filter));
    }

    @Deprecated
    @Operation(summary = "Получить все курсы сотрудника")
    @GetMapping("/attached")
    public ResponseEntity<List<UserCourseResponseDTO>> employeeCourse(@RequestParam("employeeId") String employeeId) {
        CourseEmployeeFilter filter = new CourseEmployeeFilter();
        filter.setEmployeeId(employeeId);
        return ResponseEntity.ok(courseEmployeeService.getList(filter));
    }
}
