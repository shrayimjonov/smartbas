package uz.davrbank.educontrolservice.feign.eduexam;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.exam.*;
import uz.davrbank.educontrolservice.feign.config.EduexamFeignServiceConfiguration;

@FeignClient(name = "eduexam-service", contextId = "exam", configuration = EduexamFeignServiceConfiguration.class)
public interface ExaminationInterfaceFeign {


    @PostMapping("/api/v1/edu/exam/examination/create")
    ExaminationDTO createExamination(@RequestBody ExamCreationRequestDto examinationDTO,
                                     @RequestParam Long departmentId);

    @GetMapping("/api/v1/edu/exam/examination/get/{id}")
    Object gettingExaminationByID(@PathVariable("id") Long id,
                                  @RequestParam(value = "departmentId", required = false) Long departmentId);

    @GetMapping("/api/v1/edu/exam/examination/get/all")
    PageResponseDTO<ExaminationDTO> totalExaminations(@RequestParam(required = false, name = "courseId") Long courseId,
                                                      @RequestParam(required = false, name = "cloudresourceId") Long cloudresourceId,
                                                      @RequestParam(required = false, name = "type") String type,
                                                      @RequestParam("departmentId") Long departmentId,
                                                      @PageableDefault(size = 20, sort = "id") Pageable pageable);

    @PutMapping("/api/v1/edu/exam/examination/update/info")
    Object updateExamination(@RequestParam Long id,
                             @RequestBody ExaminationRequestDto examinationDTO,
                             @RequestParam Long departmentId);

    @DeleteMapping("/api/v1/edu/exam/examination/remove/{id}")
    void deleteExamination(@PathVariable Long id);

    @GetMapping("/api/v1/edu/exam/examination/my-exams/{courseId}")
    Object getAvailableExams(@PathVariable(name = "courseId", required = false) Long courseId,
                            @RequestParam("departmentId") Long departmentId);

    @GetMapping("/api/v1/edu/exam/examination/my-exams")
    PageResponseDTO<ExaminationDTO> getMyExams(
            @RequestParam("userId") String userId,
            @RequestParam(value = "courseId", required = false) Long courseId,
            @RequestParam(value = "cloudresourceId", required = false) Long cloudresourceId,
            @RequestParam(value = "status", required = false) String status,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    );

}
