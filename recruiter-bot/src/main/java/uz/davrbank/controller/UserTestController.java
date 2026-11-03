package uz.davrbank.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.request.NotesRequestDto;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.request.UserTestRequestDto;
import uz.davrbank.dto.response.*;
import uz.davrbank.repository.FileUploadRepository;
import uz.davrbank.service.UserTestService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tests")
public class UserTestController {
    private final UserTestService userTestService;
    private final FileUploadRepository fileUploadRepository;

    public UserTestController(UserTestService userTestService, FileUploadRepository fileUploadRepository) {
        this.userTestService = userTestService;
        this.fileUploadRepository = fileUploadRepository;
    }

    @PostMapping("/submit")
    public ResultData<UserTestResultResponse> submitTest(@Valid @RequestBody UserTestRequestDto testRequestDto) {
        return userTestService.submitTest(testRequestDto);
    }

    @GetMapping
    public ResultData<TestSummaryResponse> getTestSummary() {
        return userTestService.calculateTestSummary();
    }

    @GetMapping("/daily")
    public ResultData<List<DailyTestSummaryResponse>> getDailyTestSummary(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return userTestService.calculateDailyTestSummary(date);
    }

    @GetMapping("/lastSixMonthsStatistics")
    public ResponseEntity<ResultData<List<LastSixMonthsStatisticsResponse>>> getLastSixMonthsStatistics() {
        var result = userTestService.calculateMonthlyStatisticsForLastSixMonths();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user-test-details")
    public ResponseEntity<ResultData<PaginationResponse<UserTestDetailResponse>>> getAllUserTestDetails(PaginationRequest request) {
        var result = userTestService.getAllUserTestDetails(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<ResultData<ResponseDto>> updateUserNotes(
            @PathVariable Long id,
            @Valid @RequestBody NotesRequestDto notesRequestDto) {
        var result = userTestService.updateUserNotes(id, notesRequestDto.getNotes());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllFiles() {
        return ResponseEntity.ok(fileUploadRepository.findAll());
    }

}
