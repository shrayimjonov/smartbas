package uz.davrbank.service;

import org.springframework.data.domain.Page;
import uz.davrbank.dto.request.CreateTestRequest;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.CreateTestResponse;
import uz.davrbank.dto.response.ResultData;

import java.util.List;

public interface CreateTestService {
    ResultData<CreateTestResponse> createTest(CreateTestRequest dto);

    ResultData<CreateTestResponse> updateTest(Long id, CreateTestRequest dto);

    ResultData<Page<CreateTestResponse>> getAllTest(Long departmentId, int page, int size);

    ResultData<List<CreateTestResponse>> getAvailableTests(Long departmentId, String language);

    ResultData<ResponseDto> deleteTestByDepartmentIdAndId(Long departmentId, Long id);


}