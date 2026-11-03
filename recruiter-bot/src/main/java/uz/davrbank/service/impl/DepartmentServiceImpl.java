package uz.davrbank.service.impl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.request.DepartmentDto;
import uz.davrbank.dto.response.DepartmentResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.Department;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.exception.condition.BadRequestException;
import uz.davrbank.mapper.DepartmentMapper;
import uz.davrbank.repository.CreateTestRepository;
import uz.davrbank.repository.DepartmentRepository;
import uz.davrbank.repository.specification.DepartmentSpecification;
import uz.davrbank.service.DepartmentService;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;
    private final CreateTestRepository createTestRepository;

    public DepartmentServiceImpl(DepartmentRepository repository, DepartmentMapper mapper,CreateTestRepository createTestRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.createTestRepository = createTestRepository;
    }


    @Override
    public ResultData<DepartmentResponse> create(@Valid DepartmentDto departmentDto) {
        Department department = mapper.toEntity(departmentDto);
        Department savedDepartment = repository.save(department);
        return new ResultData<>(mapper.toDto(savedDepartment));
    }

    @Override
    @Transactional
    public ResultData<ResponseDto> delete(Long id) {
        Department department = findDepartmentById(id);
        if (createTestRepository.existsByDepartmentId(id)) {
            throw new BadRequestException("Department with id: " + id + " is associated with existing CreateTest records and cannot be deleted.");
        }
        repository.deleteById(department.getId());
        return new ResultData<>(new ResponseDto(200, "Successfully deleted Department by Id: " + id));
    }

    @Override
    public ResultData<DepartmentResponse> findById(Long id) {
        return new ResultData<>(mapper.toDto(findDepartmentById(id)));
    }

    @Override
    public ResultData<PaginationResponse<DepartmentResponse>> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Department> departmentPage = repository.findAll(DepartmentSpecification.orderByIdDesc(), pageable);
        PaginationResponse<DepartmentResponse> response = PaginationResponse.fromPage(departmentPage, mapper::toDto);
        return new ResultData<>(response);
    }

    @Override
    public Department findDepartmentById(Long departmentId) {
        return repository.findById(departmentId).orElseThrow(() -> new NotFoundException("Department not found with id : " + departmentId));
    }
}