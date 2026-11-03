package uz.davrbank.service;

import jakarta.validation.constraints.NotNull;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.request.DepartmentDto;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.DepartmentResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.Department;

public interface DepartmentService {
    ResultData<DepartmentResponse> create(DepartmentDto departmentDto);

    ResultData<ResponseDto> delete(Long id);

    ResultData<DepartmentResponse> findById(Long id);

    ResultData<PaginationResponse<DepartmentResponse>> findAll(PaginationRequest paginationRequest);

    Department findDepartmentById(@NotNull(message = "Department ID must not be null") Long departmentId);
}
