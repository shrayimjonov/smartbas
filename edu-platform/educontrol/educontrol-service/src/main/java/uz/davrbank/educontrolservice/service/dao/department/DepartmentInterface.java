package uz.davrbank.educontrolservice.service.dao.department;


import uz.davrbank.educontrolservice.aggregate.dto.department.DepartmentRequestDTO;

import java.util.List;

public interface DepartmentInterface {

    Object createDepartment(DepartmentRequestDTO departmentRequestDTO);

    Object getAllDepartments(int page, int size, String search);

    Object getOneDepartment(Long id);

    Object deleteDepartment(Long id);

    Object updateDepartment(Long id, DepartmentRequestDTO departmentRequestDTO);

    List<Object> getAllDepartmentWithoutPagination();

    String testDepartment();
}
