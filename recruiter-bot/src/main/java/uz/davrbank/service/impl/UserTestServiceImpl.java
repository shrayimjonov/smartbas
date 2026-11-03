package uz.davrbank.service.impl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.davrbank.dto.DashboardDTO;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.request.UserAnswerRequestDto;
import uz.davrbank.dto.request.UserTestRequestDto;
import uz.davrbank.dto.response.*;
import uz.davrbank.entity.*;
import uz.davrbank.enums.ResponseStatus;
import uz.davrbank.repository.*;
import uz.davrbank.service.UserTestService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class UserTestServiceImpl implements UserTestService {
    private final UserTestResultRepository userTestResultRepository;
    private final CreateTestRepository createTestRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserRepository userRepository;
    private final FileUploadRepository fileUploadRepository;


    public UserTestServiceImpl(UserTestResultRepository userTestResultRepository, CreateTestRepository createTestRepository, UserAnswerRepository userAnswerRepository, UserRepository userRepository, FileUploadRepository fileUploadRepository) {
        this.userTestResultRepository = userTestResultRepository;
        this.createTestRepository = createTestRepository;
        this.userAnswerRepository = userAnswerRepository;
        this.userRepository = userRepository;
        this.fileUploadRepository = fileUploadRepository;
    }


    @Override
    public ResultData<UserTestResultResponse> submitTest(@Valid UserTestRequestDto testRequestDto) {
        int correctAnswersCount = 0;
        Optional<User> userOpt = userRepository.findById(testRequestDto.getUserId());
        if (userOpt.isEmpty()) {
            return new ResultData<>(false, ResponseStatus.FAILED, "User not found.");
        }
        User user = userOpt.get();
        for (UserAnswerRequestDto userAnswer : testRequestDto.getAnswers()) {
            Optional<CreateTest> surveyFormOpt = createTestRepository.findById(userAnswer.getQuestionId());
            if (surveyFormOpt.isPresent()) {
                CreateTest createTest = surveyFormOpt.get();
                boolean isCorrect = userAnswer.getSelectedAnswer().equals(createTest.getCorrectAnswer());
                if (isCorrect) {
                    correctAnswersCount++;
                }
                UserAnswer answerEntity = UserAnswer.builder()
                        .user(user)
                        .createTest(createTest)
                        .selectedAnswer(userAnswer.getSelectedAnswer())
                        .points(isCorrect ? 1 : 0)
                        .createdAt(LocalDateTime.now())
                        .build();
                userAnswerRepository.save(answerEntity);
            } else {
                log.warn("Question with ID {} not found", userAnswer.getQuestionId());
            }
        }
        int totalQuestions = testRequestDto.getAnswers().size();
        if (totalQuestions == 0) {
            return new ResultData<>(false, ResponseStatus.FAILED, "No answers submitted.");
        }
        double percentage = ((double) correctAnswersCount / totalQuestions) * 100;
        int totalPoints = (int) ((double) correctAnswersCount / totalQuestions * 100);
        TestResult testResult = new TestResult();
        testResult.setUser(user);
        testResult.setCorrectAnswers(correctAnswersCount);
        testResult.setTotalQuestions(totalQuestions);
        testResult.setPercentage(percentage);
        testResult.setTotalPoints(totalPoints);
        testResult.setCreatedAt(LocalDateTime.now());
        userTestResultRepository.save(testResult);
        UserTestResultResponse resultResponse = UserTestResultResponse.builder()
                .correctAnswers(correctAnswersCount)
                .totalQuestions(totalQuestions)
                .percentage(percentage)
                .totalPoints(totalPoints)
                .build();
        return new ResultData<>(resultResponse);
    }

    @Override
    public ResultData<TestSummaryResponse> calculateTestSummary() {
        try {
            DashboardDTO dashboardData = userTestResultRepository.getUserDashboard(
                    null,  //  all  statistics
                    50     // Points threshold for passing the test
            );
            TestSummaryResponse response = TestSummaryResponse.builder()
                    .numberOfTestTakers(dashboardData.getTotalDistinctUsers())      // Total distinct users
                    .passedCount(dashboardData.getTotalPassedTests())               // Total passed tests
                    .failedCount(dashboardData.getTotalFailedTests())// Total failed tests
                    .departmentCount(dashboardData.getDepartmentCount())
                    .build();
            return new ResultData<>(response);
        } catch (Exception e) {
            return new ResultData<>(false, ResponseStatus.FAILED, "Failed to calculate test summary.");
        }
    }

    @Override
    public ResultData<List<DailyTestSummaryResponse>> calculateDailyTestSummary(LocalDate date) {
        try {
            LocalDate startDate = date != null ? date : LocalDate.now();
            Integer passingPoints = 50;

            DashboardDTO dashboardData = userTestResultRepository.getUserDashboard(
                    startDate,              // Date for daily summary
                    passingPoints          // Points threshold for passing
            );
            if (dashboardData != null) {
                double testTakersPercentage = (dashboardData.getTotalDistinctUsers() > 0)
                        ? ((double) dashboardData.getDailyDistinctUsers() / dashboardData.getTotalDistinctUsers()) * 100
                        : 0;
                DailyTestSummaryResponse response = DailyTestSummaryResponse.builder()
                        .date(startDate.atStartOfDay())
                        .dailyTestTakers(dashboardData.getDailyDistinctUsers())
                        .dailyPassedCount(dashboardData.getDailyPassedTests())
                        .dailyFailedCount(dashboardData.getDailyFailedTests())
                        .departmentCount(dashboardData.getDepartmentCount())
                        .testTakersPercentage(testTakersPercentage)
                        .build();
                return new ResultData<>(List.of(response));
            } else {
                return new ResultData<>(false, ResponseStatus.INCORRECT_DATA, "No data found for the given date.");
            }
        } catch (Exception e) {
            return new ResultData<>(false, ResponseStatus.FAILED, "Failed to calculate daily test summary.");
        }
    }

    @Override
    public ResultData<List<LastSixMonthsStatisticsResponse>> calculateMonthlyStatisticsForLastSixMonths() {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(5).withDayOfMonth(1);
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            List<Object[]> statistics = userTestResultRepository.fetchStatisticsByDateRange(startDateTime, endDateTime, 50);
            Map<String, LastSixMonthsStatisticsResponse> statisticsMap = new HashMap<>();

            for (Object[] row : statistics) {
                Integer month = (Integer) row[1];
                Long passedCount = ((Number) row[2]).longValue();
                Long failedCount = ((Number) row[3]).longValue();
                Long testTakersCount = ((Number) row[4]).longValue();

                String monthName = Month.of(month).name();
                statisticsMap.put(monthName, LastSixMonthsStatisticsResponse.builder()
                        .month(monthName)
                        .monthlyPassedCount(passedCount)
                        .monthlyFailedCount(failedCount)
                        .monthlyTestTakers(testTakersCount)
                        .build());
            }

            List<LastSixMonthsStatisticsResponse> monthlyStatistics = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                LocalDate currentMonth = startDate.plusMonths(i);
                String monthName = currentMonth.getMonth().name();
                monthlyStatistics.add(statisticsMap.getOrDefault(monthName, LastSixMonthsStatisticsResponse.builder()
                        .month(monthName)
                        .monthlyPassedCount(0L)
                        .monthlyFailedCount(0L)
                        .monthlyTestTakers(0L)
                        .build()));
            }
            return new ResultData<>(monthlyStatistics);
        } catch (Exception e) {
            return new ResultData<>(false, ResponseStatus.FAILED, "Failed to calculate monthly statistics for the last six months.");
        }
    }


    @Override
    public ResultData<PaginationResponse<UserTestDetailResponse>> getAllUserTestDetails(PaginationRequest paginationRequest) {
        try {
            Pageable pageable = paginationRequest.toPageable();
            Page<User> userPage = userRepository.findAllByOrderByIdDesc(pageable);
            List<UserTestDetailResponse> responseList = userPage.getContent().stream().map(user -> {
                Optional<TestResult> latestTestResultOpt = userTestResultRepository.findFirstByUserOrderByCreatedAtDesc(user);
                if (latestTestResultOpt.isPresent()) {
                    TestResult latestTestResult = latestTestResultOpt.get();
                    Optional<FileUpload> fileUploadOpt = fileUploadRepository.findById(user.getFile().getId());

                    return UserTestDetailResponse.builder()
                            .id(user.getId())
                            .requestDate(latestTestResult.getCreatedAt())
                            .name(user.getName())
                            .phone(user.getPhone())
                            .month(latestTestResult.getCreatedAt().getMonth().name())
                            .fileId(fileUploadOpt.map(FileUpload::getId).orElse(null))
                            .fileName(fileUploadOpt.map(FileUpload::getFileName).orElse(null))
                            .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : "null") // Handle null
                            .totalScore(latestTestResult.getTotalPoints() + " / 100")
                            .totalTests(latestTestResult.getTotalQuestions() + " / " + userTestResultRepository.countTotalTestsForUser(user.getId()))
                            .percentage(latestTestResult.getPercentage() + " %")
                            .status(latestTestResult.getTotalPoints() >= 50 ? "Passed" : "Failed")
                            .notes(user.getNotes() != null ? user.getNotes() : "")
                            .build();
                }
                return null;
            }).filter(Objects::nonNull).toList();
            PaginationResponse<UserTestDetailResponse> paginationResponse = PaginationResponse.fromPage(
                    new PageImpl<>(responseList, pageable, userPage.getTotalElements()),
                    Function.identity()
            );

            return new ResultData<>(paginationResponse);
        } catch (Exception e) {
            return new ResultData<>(false, ResponseStatus.FAILED, e.getMessage());
        }
    }

    @Override
    public ResultData<ResponseDto> updateUserNotes(Long userId, String notes) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setNotes(notes.trim());
            userRepository.save(user);
            ResponseDto responseDto = ResponseDto.builder()
                    .code(200)
                    .message("Notes updated successfully")
                    .build();
            return new ResultData<>(responseDto);
        }
        ResponseDto e = ResponseDto.builder()
                .code(404)
                .message("User not found")
                .build();
        return new ResultData<>(false, ResponseStatus.FAILED, e.getMessage());
    }
}