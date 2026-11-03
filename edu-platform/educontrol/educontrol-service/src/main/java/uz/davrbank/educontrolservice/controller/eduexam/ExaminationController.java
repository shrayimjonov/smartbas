package uz.davrbank.educontrolservice.controller.eduexam;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.examinations.exam.*;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.exception.condition.BusinessValidationException;
import uz.davrbank.educontrolservice.feign.EmployeeClientFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.eduexam.ExaminationInterface;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/educontrol/examination")
public class ExaminationController {

    private final ExaminationInterface examinationInterface;
    private final EmployeeClientFeign employeeClientFeign;
    private final JwtTokenService jwtTokenService;

    public ExaminationController(ExaminationInterface examinationInterface, EmployeeClientFeign employeeClientFeign, JwtTokenService jwtTokenService) {
        this.examinationInterface = examinationInterface;
        this.employeeClientFeign = employeeClientFeign;
        this.jwtTokenService = jwtTokenService;
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @PostMapping
    public ResponseEntity<ApiResponse<ExaminationDTO>> createExamination(@RequestBody ExamCreationRequestDto examinationDTO) {
        Long departmentId = jwtTokenService.getDepartmentId();

        if(Objects.isNull(examinationDTO.getEmployeeIds())){
            List<String> employeeIds = employeeClientFeign.getEmployeeIdsByDepartment(departmentId);
            examinationDTO.setEmployeeIds(employeeIds);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Экзамен успешно создан",
                        examinationInterface.createExamination(examinationDTO, departmentId)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT', 'ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> gettingExaminationByID(@PathVariable("id") Long id) {
        Long departmentId;

        List<String> roles = jwtTokenService.roles();

        if (roles.contains("ROLE_SUPERADMIN")) {
            departmentId = null;
        } else {
            departmentId = jwtTokenService.getDepartmentId();
            if (departmentId == null) {
                throw new BusinessValidationException("User is not assigned to any department.");
            }
        }

        return ResponseEntity.ok(ApiResponse.of("Информация об экзамене получена",
                examinationInterface.getExaminationById(id, departmentId)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT', 'ROLE_EMPLOYEE')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageResponseDTO<ExaminationDTO>>> getAllExamination(
            @RequestParam(required = false, name = "courseId") Long courseId,
            @RequestParam(required = false, name = "cloudresourceId") Long cloudresourceId,
            @RequestParam(required = false, name = "type") String type,
            @ParameterObject
            @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {

        Long departmentIdToFilter;

        List<String> roles = jwtTokenService.roles();

        if (roles.contains("ROLE_SUPERADMIN")) {
            departmentIdToFilter = null;
        } else {
            departmentIdToFilter = jwtTokenService.getDepartmentId();

            if (departmentIdToFilter == null) {
                //return empty list or throw error
                throw new BusinessValidationException("User not assigned to any department.");
            }
        }

        return ResponseEntity.ok(
                ApiResponse.of("Список экзаменов получен",
                        examinationInterface.totalExaminations(
                                courseId, cloudresourceId, type, departmentIdToFilter, pageable)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateExaminationInfo(@PathVariable("id") Long id,
                                                                     @RequestBody ExaminationRequestDto examinationDTO) {
        Long departmentId = jwtTokenService.getDepartmentId();
        return ResponseEntity.ok(ApiResponse.of("Экзамен успешно обновлён",
                examinationInterface.updateExamination(examinationDTO, id, departmentId)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removeExamination(@PathVariable("id") Long id) {
        examinationInterface.deleteExamination(id);
        return ResponseEntity.ok(ApiResponse.of("Экзамен успешно удалён"));
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @Operation(summary = "Получить экзамены текущего пользователя (My Exams)")
    @GetMapping("/my-exams")
    public ResponseEntity<ApiResponse<PageResponseDTO<ExaminationDTO>>> getMyExams(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long cloudresourceId,
            @RequestParam(required = false) String status,
            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        String userId = jwtTokenService.getCurrentUserId();


        return ResponseEntity.ok(ApiResponse.of("Список моих экзаменов",
                examinationInterface.getMyExams(userId, courseId, cloudresourceId, status, pageable)
        ));
    }

}
