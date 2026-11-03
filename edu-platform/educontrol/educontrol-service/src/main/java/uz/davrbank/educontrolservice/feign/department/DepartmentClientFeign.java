package uz.davrbank.educontrolservice.feign.department;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.department.DepartmentRequestDTO;
import uz.davrbank.educontrolservice.feign.config.DepartmentFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "department-service", contextId = "department", configuration = DepartmentFeignServiceConfiguration.class)
public interface DepartmentClientFeign {

    @PostMapping("/api/v1/department/create")
    Object createDepartment(@RequestBody DepartmentRequestDTO departmentRequestDTO);

    @GetMapping("/api/v1/department/get/all")
    Object getAllDepartments(@RequestParam int page,
                             @RequestParam int size,
                             @RequestParam String name);

    @GetMapping("/api/v1/department/get/one/{id}")
    Object getOneDepartment(@PathVariable Long id);

    @DeleteMapping("/api/v1/department/delete/{id}")
    Object deleteDepartment(@PathVariable Long id);

    @PutMapping("/api/v1/department/update/{id}")
    Object updateDepartment(@PathVariable Long id,
                            @RequestBody DepartmentRequestDTO departmentRequestDTO);

    @GetMapping("/api/v1/department/all")
    List<Object> getAllDepartmentWithoutPagination();

    @GetMapping("/api/v1/department/test")
    String testDepartment();
}
