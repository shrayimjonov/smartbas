package uz.davrbank.educontrolservice.service.cases.eduexam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.exam.*;
import uz.davrbank.educontrolservice.exception.condition.DepartmentIdNotFoundException;
import uz.davrbank.educontrolservice.feign.eduexam.ExaminationInterfaceFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.eduexam.ExaminationInterface;

import java.util.Objects;

@Service
@Slf4j
public class ExaminationService implements ExaminationInterface {
    private final ExaminationInterfaceFeign examinationInterfaceFeign;
    private final JwtTokenService jwtTokenService;

    public ExaminationService(ExaminationInterfaceFeign examinationInterfaceFeign,
                              JwtTokenService jwtTokenService) {
        this.examinationInterfaceFeign = examinationInterfaceFeign;
        this.jwtTokenService = jwtTokenService;
    }


    @Override
    public ExaminationDTO createExamination(ExamCreationRequestDto examinationDTO, Long departmentId) {
        log.info("REQUEST (createExamination) FROM [eduexam-service]");

        return examinationInterfaceFeign.createExamination(examinationDTO, departmentId);

    }

    @Override
    public Object updateExamination(ExaminationRequestDto examinationDTO, Long id, Long departmentId) {
        log.info("REQUEST (updateExamination) FROM [eduexam-service]");
        return examinationInterfaceFeign.updateExamination(id, examinationDTO, departmentId);
    }

    @Override
    public Object getExaminationById(Long id, Long departmentId) {
        log.info("REQUEST (getExaminationById) FROM [eduexam-service]");
        return examinationInterfaceFeign.gettingExaminationByID(id, departmentId);
    }

    @Override
    public PageResponseDTO<ExaminationDTO> totalExaminations(Long courseId,
                                                             Long cloudresourceId,
                                                             String type,
                                                             Long departmentId,
                                                             Pageable pageable) {
        log.info("REQUEST (totalExaminations) FROM [eduexam-service]");
        return examinationInterfaceFeign.totalExaminations(courseId, cloudresourceId, type, departmentId, pageable);
    }

    @Override
    public void deleteExamination(Long id) {
        log.info("REQUEST (deleteExamination) FROM [eduexam-service]");
        examinationInterfaceFeign.deleteExamination(id);
    }

    @Override
    public PageResponseDTO<ExaminationDTO> getMyExams(String userId, Long courseId, Long cloudresourceId, String status, Pageable pageable) {
        log.info("REQUEST (getAvailableExamination) FROM [eduexam-service]");


        Long departmentId = jwtTokenService.getDepartmentId();
        if(Objects.isNull(departmentId))
            throw new DepartmentIdNotFoundException("Department id is not found");

        return examinationInterfaceFeign.getMyExams(userId, courseId, cloudresourceId, status, pageable);
    }


}
