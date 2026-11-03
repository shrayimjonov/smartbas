package uz.davrbank.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.dto.request.CreateTestRequest;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.CreateTestResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.service.CreateTestService;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class CreateTestController {
    private final CreateTestService createTestService;

    public CreateTestController(CreateTestService createTestService) {
        this.createTestService = createTestService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResultData<CreateTestResponse>> createTest(@Valid @RequestBody CreateTestRequest dto) {
        var result = createTestService.createTest(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResultData<CreateTestResponse>> updateTest(@PathVariable Long id, @Valid @RequestBody CreateTestRequest dto) {
        var result = createTestService.updateTest(id, dto);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{departmentId}")
    public ResultData<Page<CreateTestResponse>> getTestsByDepartment(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return createTestService.getAllTest(departmentId, page, size);
    }

    @GetMapping("/availableTests")
    public ResponseEntity<ResultData<List<CreateTestResponse>>> getAvailableTests(
            @RequestParam Long departmentId,
            @RequestParam String language) {
        var result = createTestService.getAvailableTests(departmentId, language);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{departmentId}/{id}")
    public ResponseEntity<ResultData<ResponseDto>> deleteTestByDepartmentIdAndId(
            @PathVariable Long departmentId, @PathVariable Long id) {
        var result = createTestService.deleteTestByDepartmentIdAndId(departmentId, id);
        return ResponseEntity.ok(result);
    }



}
