package uz.davrbank.service;

import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.request.UserTestRequestDto;
import uz.davrbank.dto.response.*;

import java.time.LocalDate;
import java.util.List;

public interface UserTestService {
    ResultData<UserTestResultResponse> submitTest(UserTestRequestDto testRequestDto);

    ResultData<TestSummaryResponse> calculateTestSummary();

    ResultData<List<DailyTestSummaryResponse>> calculateDailyTestSummary(LocalDate date);

    ResultData<List<LastSixMonthsStatisticsResponse>> calculateMonthlyStatisticsForLastSixMonths();

    ResultData<PaginationResponse<UserTestDetailResponse>> getAllUserTestDetails(PaginationRequest paginationRequest);

    ResultData<ResponseDto> updateUserNotes(Long id, String notes);


}
