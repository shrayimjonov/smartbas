package uz.davrbank.educontrolservice.controller.course;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CourseEmployeeFilter;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.course.CourseEmployeeInterface;

@RestController
@RequestMapping("/api/v1/educontrol/course-service/course/employee")
public class CourseEmployeeController {
    private final CourseEmployeeInterface courseEmployeeInterface;

    public CourseEmployeeController(CourseEmployeeInterface courseEmployeeInterface) {
        this.courseEmployeeInterface = courseEmployeeInterface;
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<Object>> startedCourse(@RequestParam Long courseId) {
        return ResponseEntity.ok(ApiResponse.of("Курс начат",
                courseEmployeeInterface.startedCourse(courseId)));
    }

    @PostMapping("/complete")
    public ResponseEntity<ApiResponse<Object>> completedCourse(@RequestParam Long courseId) {
        return ResponseEntity.ok(ApiResponse.of("Курс завершён",
                courseEmployeeInterface.completedCourse(courseId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getPageable(@ParameterObject CourseEmployeeFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список прикреплённых сотрудников получен",
                courseEmployeeInterface.getPageable(filter)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getList(@ParameterObject CourseEmployeeFilter filter) {
        return ResponseEntity.ok(ApiResponse.of("Список прикреплённых сотрудников получен",
                courseEmployeeInterface.getList(filter)));
    }

    @GetMapping("/attached")
    public ResponseEntity<ApiResponse<Object>> employeeCourse() {
        return ResponseEntity.ok(ApiResponse.of("Список курсов сотрудника получен",
                courseEmployeeInterface.getEmployeeStartedCourses()));
    }
}
