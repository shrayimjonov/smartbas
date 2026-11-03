package uz.davrbank.mapper;

import org.mapstruct.Mapper;
import uz.davrbank.dto.request.DepartmentDto;
import uz.davrbank.dto.response.DepartmentResponse;
import uz.davrbank.entity.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toEntity(DepartmentDto departmentDto);

    DepartmentResponse toDto(Department savedDepartment);
}
