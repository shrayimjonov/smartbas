package uz.davrbank.educontrolservice.controller.department;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.department.DepartmentRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.department.DepartmentInterface;


@RestController
@RequestMapping("/api/v1/educontrol/department")
public class DepartmentController {

    private final DepartmentInterface departmentInterface;

    public DepartmentController(DepartmentInterface departmentInterface) {
        this.departmentInterface = departmentInterface;
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createDepartment(@RequestBody DepartmentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Департамент успешно создан", departmentInterface.createDepartment(dto)));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DEPARTMENT', 'ROLE_HR')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> getAllDepartmentData(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "100") int size,
                                                                    @RequestParam(required = false) String search) {
        return ResponseEntity.ok(ApiResponse.of("Список департаментов получен", departmentInterface.getAllDepartments(page, size, search)));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DEPARTMENT', 'ROLE_HR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getOneDepartmentData(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о департаменте получена", departmentInterface.getOneDepartment(id)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteDepartmentData(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Департамент успешно удален", departmentInterface.deleteDepartment(id)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateDepartmentData(@PathVariable Long id, @RequestBody DepartmentRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.of("Департамент успешно обновлен", departmentInterface.updateDepartment(id, dto)));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DEPARTMENT', 'ROLE_HR')")
    @GetMapping("/all/without-pagination")
    public ResponseEntity<ApiResponse<Object>> getAllDepartmentWithoutPagination() {
        return ResponseEntity.ok(ApiResponse.of("Список всех департаментов получен", departmentInterface.getAllDepartmentWithoutPagination()));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<Void>> testDepartment() {
        return ResponseEntity.ok(ApiResponse.of(departmentInterface.testDepartment()));
    }
}
