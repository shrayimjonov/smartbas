package uz.davrbank.educontrolservice.feign.course;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.step.StepDTO;
import uz.davrbank.educontrolservice.feign.config.CourseFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "course-service", contextId = "step", configuration = CourseFeignServiceConfiguration.class)
public interface StepFeign {

    @PostMapping("/course-service/api/v1/step")
    Object createStep(@RequestBody StepDTO stepDTO);

    @PutMapping("/course-service/api/v1/step/{id}")
    Object updateStep(@PathVariable Long id,
                      @RequestBody StepDTO stepDTO);

    @GetMapping("/course-service/api/v1/step/list")
    List<Object> getAllCoursesStep(@RequestParam("courseId") Long courseId);

    @DeleteMapping("/course-service/api/v1/step/{id}")
    Object deleteStep(@PathVariable Long id);

    @GetMapping("/course-service/api/v1/step/{id}")
    Object getStepById(@PathVariable Long id);

    @GetMapping("/course-service/api/v1/step/files")
    List<Object> getAllFilesInfo(@RequestParam("stepId") Long stepId);
}
