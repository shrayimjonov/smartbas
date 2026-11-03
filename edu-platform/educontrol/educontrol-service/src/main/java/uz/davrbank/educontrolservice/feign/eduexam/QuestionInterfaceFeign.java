package uz.davrbank.educontrolservice.feign.eduexam;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.exam.TestAnswersRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.question.QuestionCreationDTO;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.question.QuestionUpdateRequestDTO;
import uz.davrbank.educontrolservice.feign.config.EduexamFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "eduexam-service", contextId = "question", configuration = EduexamFeignServiceConfiguration.class)
public interface QuestionInterfaceFeign {

    @PostMapping("/api/v1/edu/exam/test/store")
    Object saveQuestion(@RequestBody QuestionCreationDTO questionDTO);

    @GetMapping("/api/v1/edu/exam/test/get/by/{id}")
    Object getQuestionById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/edu/exam/test/get/all")
    List<Object> getAllQuestionFromDB(@RequestParam Long examId);

    @PutMapping("/api/v1/edu/exam/test/update/data/{id}")
    Object updateQuestionAndSaveData(@PathVariable("id") Long id, @RequestBody QuestionUpdateRequestDTO questionDTO);

    @DeleteMapping("/api/v1/edu/exam/test/remove/data/{id}")
    Object deleteQuestionById(@PathVariable("id") Long id);

    @PostMapping("/api/v1/edu/exam/test/check/answers")
    Object checkAnswers(@RequestParam String userId,
                        @RequestParam Long examId,
                        @RequestBody List<TestAnswersRequestDTO> testAnswersRequestDTOS);
}
