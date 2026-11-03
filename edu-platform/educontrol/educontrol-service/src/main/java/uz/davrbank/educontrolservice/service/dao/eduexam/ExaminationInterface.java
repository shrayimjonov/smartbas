package uz.davrbank.educontrolservice.service.dao.eduexam;


import org.springframework.data.domain.Pageable;

import uz.davrbank.educontrolservice.aggregate.dto.examinations.exam.*;

public interface ExaminationInterface {

    ExaminationDTO createExamination(ExamCreationRequestDto examinationDTO, Long departmentId);

    Object updateExamination(ExaminationRequestDto examinationDTO, Long id, Long departmentId);

    Object getExaminationById(Long id, Long departmentId);

    PageResponseDTO<ExaminationDTO> totalExaminations(Long courseId,
                                                      Long cloudresourceId,
                                                      String type,
                                                      Long departmentId,
                                                      Pageable pageable);

    void deleteExamination(Long id);

    PageResponseDTO<ExaminationDTO> getMyExams(String userId, Long courseId, Long cloudresourceId, String status, Pageable pageable);
}
