package uz.davrbank.educontrolservice.feign.course;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.educontrolservice.feign.config.CourseFeignServiceConfiguration;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CourseEmployeeFilter;

import java.util.List;

@FeignClient(name = "course-service", contextId = "courseEmployee", configuration = CourseFeignServiceConfiguration.class)
public interface CourseEmployeeFeign {

    @PostMapping("/course-service/api/v1/course/employee/start")
    Object startedCourse(@RequestParam Long courseId,
                         @RequestParam String employeeId);

    @PostMapping("/course-service/api/v1/course/employee/complete")
    Object completedCourse(@RequestParam Long courseId,
                           @RequestParam String employeeId);

    @GetMapping("/course-service/api/v1/course/employee")
    Object getPageable(@SpringQueryMap CourseEmployeeFilter filter);

    @GetMapping("/course-service/api/v1/course/employee/list")
    Object getList(@SpringQueryMap CourseEmployeeFilter filter);

    @GetMapping("/course-service/api/v1/course/employee/attached")
    List<Object> attachedCourse(@RequestParam String employeeId);

}
