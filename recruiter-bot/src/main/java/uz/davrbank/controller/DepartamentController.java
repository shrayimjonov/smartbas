package uz.davrbank.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.request.DepartmentDto;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.DepartmentResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.service.DepartmentService;

@RestController
@RequestMapping("/api/department")
public class DepartamentController {
    private final DepartmentService departmentService;

    public DepartamentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    @PostMapping()
    public ResponseEntity<ResultData<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        var result = departmentService.create(departmentDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResultData<ResponseDto>> deleteDepartment(@PathVariable Long id) {
        var result = departmentService.delete(id);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<ResultData<DepartmentResponse>> findById(@PathVariable Long id) {
        var result = departmentService.findById(id);
        return ResponseEntity.ok(result);
    }
    @GetMapping()
    public ResponseEntity<ResultData<PaginationResponse<DepartmentResponse>>> findAll(PaginationRequest request) {
        var result = departmentService.findAll(request);
        return ResponseEntity.ok(result);
    }



}
