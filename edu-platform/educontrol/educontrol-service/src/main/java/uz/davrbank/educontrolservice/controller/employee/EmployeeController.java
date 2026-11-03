package uz.davrbank.educontrolservice.controller.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.employee.EmployeeInterface;

import java.util.List;

//@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DAVR', 'ROLE_HR')")
@RestController
@RequestMapping("/api/v1/educontrol/employee")
public class EmployeeController {

    private final EmployeeInterface employeeInterface;

    public EmployeeController(EmployeeInterface employeeInterface) {
        this.employeeInterface = employeeInterface;
    }


    @PostMapping("/merge")
    public ResponseEntity<ApiResponse<Object>> mergeEmployeeDepartment(@RequestParam("employee_id") String employeeId,
                                                                       @RequestParam("department_id") Long departmentId,
                                                                       @RequestParam("position_id") Long positionId
                                                                              ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Сотрудник успешно привязан к департаменту",
                        employeeInterface.mergeEmployeeDepartment(employeeId, departmentId, positionId)));
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PutMapping("/update/merged")
    public ResponseEntity<ApiResponse<Object>> updateMergedEmployeeDepartment(@RequestParam("department_id") Long departmentId,
                                                                              @RequestParam("position_id") Long positionId) {
        return ResponseEntity.ok(ApiResponse.of("Привязка сотрудника успешно обновлена",
                employeeInterface.updateMergedEmployeeDepartment(departmentId, positionId)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Object>> getAllEmployeeData(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "100") int size) {
        return ResponseEntity.ok(ApiResponse.of("Список сотрудников получен",
                employeeInterface.getAllEmployees(page, size)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT', 'ROLE_EMPLOYEE)")
    @GetMapping("/get/one/{id}")
    public ResponseEntity<ApiResponse<Object>> getOneEmployeeData(@PathVariable("id") String uuid) {
        return ResponseEntity.ok(ApiResponse.of("Информация о сотруднике получена",
                employeeInterface.getOneEmployee(uuid)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @GetMapping("/get/by/department/{id}")
    public ResponseEntity<ApiResponse<Object>> getAllEmployeeByDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Список сотрудников департамента получен",
                employeeInterface.getAllEmployeeByDepartmentId(id)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/get/all/role")
    public ResponseEntity<ApiResponse<Object>> getAllRoles() {
        return ResponseEntity.ok(ApiResponse.of("Список ролей получен",
                employeeInterface.getAllRoles()));
    }

    @GetMapping("/get/not/in/department")
    public ResponseEntity<ApiResponse<Object>> proxyEmployees(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "100") int size,
                                                              @RequestParam(defaultValue = "id,desc") String sort) {
        return ResponseEntity.ok(ApiResponse.of("Список сотрудников без департамента получен",
                employeeInterface.getAllEmployeeNotInDepartment(page, size, sort)));
    }


    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<Void>> testEmpController() {
        return ResponseEntity.ok(ApiResponse.of(employeeInterface.testEmployee()));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @GetMapping("/get/all/db")
    public ResponseEntity<ApiResponse<Object>> getAllEmployeeFromDB() {
        return ResponseEntity.ok(ApiResponse.of("Список сотрудников из БД получен",
                employeeInterface.getAllFromDB()));
    }

    @GetMapping("/ids/by-department/{departmentId}")
    public ResponseEntity<List<String>> getEmployeeIdsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeInterface.getEmployeeIdsByDepartment(departmentId));
    }
}
