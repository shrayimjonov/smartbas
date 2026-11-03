package uz.davrbank.educontrolservice.controller.eduexam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.exam.TestAnswersRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.question.QuestionCreationDTO;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.question.QuestionUpdateRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.eduexam.QuestionInterface;

import java.util.List;


@RestController
@RequestMapping("/api/v1/educontrol/question")
public class QuestionController {

    private final QuestionInterface questionInterface;

    public QuestionController(QuestionInterface questionInterface) {
        this.questionInterface = questionInterface;
    }


    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> saveQuestion(@RequestBody QuestionCreationDTO questionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Вопрос успешно сохранён",
                        questionInterface.saveQuestion(questionDTO)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getQuestionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о вопросе получена",
                questionInterface.getQuestionById(id)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> getAllQuestionsFromDB(@RequestParam Long examId) {
        return ResponseEntity.ok(ApiResponse.of("Список вопросов получен",
                questionInterface.getAllQuestions(examId)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateQuestionAndSaveData(@PathVariable("id") Long id, @RequestBody QuestionUpdateRequestDTO questionDTO) {
        return ResponseEntity.ok(ApiResponse.of("Вопрос успешно обновлён",
                questionInterface.updateQuestion(id, questionDTO)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteQuestionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.of("Вопрос успешно удалён",
                questionInterface.deleteQuestion(id)));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<Object>> submitExamination(@RequestParam Long examId,
                                                                 @RequestBody List<TestAnswersRequestDTO> testAnswersRequestDTOS) {
        return ResponseEntity.ok(ApiResponse.of("Экзамен успешно сдан",
                questionInterface.submitExamination(examId, testAnswersRequestDTOS)));
    }

}