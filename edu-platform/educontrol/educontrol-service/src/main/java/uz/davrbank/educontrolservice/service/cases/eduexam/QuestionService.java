package uz.davrbank.educontrolservice.service.cases.eduexam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.exam.TestAnswersRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.question.QuestionCreationDTO;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.question.QuestionUpdateRequestDTO;
import uz.davrbank.educontrolservice.feign.eduexam.QuestionInterfaceFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.eduexam.QuestionInterface;

import java.util.List;


@Slf4j
@Service
public class QuestionService implements QuestionInterface {

    private final QuestionInterfaceFeign questionInterfaceFeign;
    private final JwtTokenService jwtTokenService;

    public QuestionService(QuestionInterfaceFeign questionInterfaceFeign, JwtTokenService jwtTokenService) {
        this.questionInterfaceFeign = questionInterfaceFeign;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Object saveQuestion(QuestionCreationDTO questionDTO) {
        log.info("REQUEST (saveQuestion) FROM [eduexam-service]");
        return questionInterfaceFeign.saveQuestion(questionDTO);

    }

    @Override
    public Object getQuestionById(Long id) {
        log.info("REQUEST (getQuestionById) FROM [eduexam-service]");
        return questionInterfaceFeign.getQuestionById(id);
    }

    @Override
    public List<Object> getAllQuestions(Long examId) {
        log.info("REQUEST (getAllQuestions) FROM [eduexam-service]");
        return questionInterfaceFeign.getAllQuestionFromDB(examId);
    }

    @Override
    public Object deleteQuestion(Long id) {
        log.info("REQUEST (deleteQuestion) FROM [eduexam-service]");
        return questionInterfaceFeign.deleteQuestionById(id);
    }

    @Override
    public Object updateQuestion(Long id, QuestionUpdateRequestDTO questionDTO) {
        log.info("REQUEST (updateQuestion) FROM [eduexam-service]");
        return questionInterfaceFeign.updateQuestionAndSaveData(id, questionDTO);
    }

    @Override
    public Object submitExamination(Long examId, List<TestAnswersRequestDTO> testAnswersRequestDTOS) {
        log.info("REQUEST (submitExamination) FROM [eduexam-service]");
        String userId = jwtTokenService.getCurrentUserId();
        return questionInterfaceFeign.checkAnswers(userId, examId, testAnswersRequestDTOS);

    }
}

