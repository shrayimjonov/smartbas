package uz.davrbank.educontrolservice.feign.course;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.educontrolservice.aggregate.dto.filter.StepEmployeeFilter;
import uz.davrbank.educontrolservice.feign.config.CourseFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "course-service", contextId = "stepEmployee", configuration = CourseFeignServiceConfiguration.class)
public interface StepEmployeeFeign {

    @PostMapping("/course-service/api/v1/step/employee/start")
    Object startedStep(@RequestParam Long stepId,
                       @RequestParam String employeeId);

    @PostMapping("/course-service/api/v1/step/employee/complete")
    Object completedStep(@RequestParam Long stepId,
                         @RequestParam String employeeId);

    @GetMapping("/course-service/api/v1/step/employee")
    Object getPageable(@SpringQueryMap StepEmployeeFilter filter);

    @GetMapping("/course-service/api/v1/step/employee/list")
    Object getList(@SpringQueryMap StepEmployeeFilter filter);

    @GetMapping("/course-service/api/v1/step/employee/attached")
    List<Object> employeeStep(@RequestParam(value = "employeeId") String employeeId);
}
