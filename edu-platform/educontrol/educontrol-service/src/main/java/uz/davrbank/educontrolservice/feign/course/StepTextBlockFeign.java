package uz.davrbank.educontrolservice.feign.course;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.step.StepTextBlockRequestDTO;
import uz.davrbank.educontrolservice.feign.config.CourseFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "course-service", contextId = "block", configuration = CourseFeignServiceConfiguration.class)
public interface StepTextBlockFeign {

    @PostMapping("/course-service/api/v1/step/text-blocks")
    Object createStepTextBlock(@RequestBody StepTextBlockRequestDTO requestDTO);

    @PutMapping("/course-service/api/v1/step/text-blocks/{id}")
    Object updateStepTextBlock(@PathVariable("id") Long blockId,
                               @RequestBody StepTextBlockRequestDTO requestDTO);

    @DeleteMapping("/course-service/api/v1/step/text-blocks/{id}")
    Object deleteStepTextBlock(@PathVariable("id") Long id);

    @GetMapping("/course-service/api/v1/step/text-blocks/list")
    List<Object> getAllStepTextBlocks(@RequestParam("stepId") Long stepId);

    @GetMapping("/course-service/api/v1/step/text-blocks/{id}")
    Object getStepTextBlockById(@PathVariable("id") Long id);
}
