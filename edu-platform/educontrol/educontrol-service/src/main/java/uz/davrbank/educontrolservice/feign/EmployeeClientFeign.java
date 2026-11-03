package uz.davrbank.educontrolservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.employee.EmployeeDepartmentRequestDTO;
import uz.davrbank.educontrolservice.feign.config.EmployeeFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "employee-service", configuration = EmployeeFeignServiceConfiguration.class)
public interface EmployeeClientFeign {

    @GetMapping("/api/v1/employee/get/all")
    Object getAllEmployees(@RequestParam("page") int page,
                           @RequestParam("size") int size,
                           @RequestParam("userId") String userId,
                           @RequestParam("roles") List<String> roles);

    @GetMapping("/api/v1/employee/get/one/{id}")
    Object getOneEmployee(@PathVariable("id") String id);

    @PostMapping("/api/v1/employee/merge")
    Object mergeEmployeeDepartment(@RequestBody EmployeeDepartmentRequestDTO dto);

    @PutMapping("/api/v1/employee/update/merged")
    Object updateMergedEmployeeDepartment(@RequestBody EmployeeDepartmentRequestDTO requestDTO);

    @GetMapping("/api/v1/employee/test")
    String testEmployee();

    @GetMapping("/api/v1/employee/get/by/department/{id}")
    List<Object> getEmployeeByDepartment(@PathVariable Long id);

    @GetMapping("/api/v1/employee/get/all/role")
    List<Object> getAllRoles();

    @GetMapping("/api/v1/employee/get/not/in/department")
    Object getEmployeeNotInDepartment(@RequestParam("page") int page,
                                      @RequestParam("size") int size,
                                      @RequestParam(defaultValue = "id,desc") String sort);

    @PostMapping("/api/v1/employee/check")
    boolean checkEmployeeInDepartment(@RequestParam("username") String username);

    @GetMapping("/api/v1/employee/get/all/db")
    Object getAllFromDB();

    @GetMapping("/api/v1/employee/ids/by-department/{departmentId}")
    List<String> getEmployeeIdsByDepartment(@PathVariable("departmentId") Long departmentId);
}
