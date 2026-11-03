package uz.davrbank.educontrolservice.feign.course;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.course.CourseRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CourseFilter;
import uz.davrbank.educontrolservice.feign.config.CourseFeignServiceConfiguration;

import java.util.Set;

@FeignClient(name = "course-service", contextId = "course", configuration = CourseFeignServiceConfiguration.class)
public interface CourseFeign {

    @PostMapping("/course-service/api/v1/course")
    Object createCourse(@RequestBody CourseRequestDTO courseRequestDTO,
                        @RequestParam("userId") String userId);

    @PutMapping("/course-service/api/v1/course/{id}")
    Object updateCourse(@PathVariable Long id,
                        @RequestBody CourseRequestDTO courseRequestDTO);

    @GetMapping("/course-service/api/v1/course")
    Object getPageable(@SpringQueryMap CourseFilter filter);

    @GetMapping("/course-service/api/v1/course/list")
    Object getList(@SpringQueryMap CourseFilter filter);

    @GetMapping("/course-service/api/v1/course/{id}")
    Object getCourseById(@PathVariable Long id);

    @DeleteMapping("/course-service/api/v1/course/{id}")
    Object deleteCourse(@PathVariable Long id);

    @PostMapping("/course-service/api/v1/course/{id}/access")
    Object accessEmployees(@PathVariable("id") Long courseId,
                           @RequestBody Set<String> employeeIds);
}
