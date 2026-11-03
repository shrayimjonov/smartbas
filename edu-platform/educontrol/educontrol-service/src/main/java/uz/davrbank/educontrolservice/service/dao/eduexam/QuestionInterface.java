package uz.davrbank.educontrolservice.service.dao.eduexam;

import uz.davrbank.educontrolservice.aggregate.dto.examinations.exam.TestAnswersRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.question.QuestionCreationDTO;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.question.QuestionUpdateRequestDTO;

import java.util.List;

public interface QuestionInterface {

    Object saveQuestion(QuestionCreationDTO questionDTO);

    Object getQuestionById(Long id);

    List<Object> getAllQuestions(Long examId);

    Object deleteQuestion(Long id);

    Object updateQuestion(Long id, QuestionUpdateRequestDTO questionDTO);


    Object submitExamination(Long examId, List<TestAnswersRequestDTO> testAnswersRequestDTOS);
}
