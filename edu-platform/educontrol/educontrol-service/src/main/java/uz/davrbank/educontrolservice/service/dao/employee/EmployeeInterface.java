package uz.davrbank.educontrolservice.service.dao.employee;


import java.util.List;

public interface EmployeeInterface {

    Object getAllEmployees(int page, int size);

    Object getOneEmployee(String id);

    Object mergeEmployeeDepartment(String employeeId, Long departmentId, Long positionId);

    Object updateMergedEmployeeDepartment(Long departmentId, Long positionId);

    String testEmployee();

    List<Object> getAllEmployeeByDepartmentId(Long departmentId);

    List<Object> getAllRoles();

    Object getAllEmployeeNotInDepartment(int page, int size, String sort);

    Object checkEmployeeInDepartment(String username);

    Object getAllFromDB();

    List<String> getEmployeeIdsByDepartment(Long departmentId);
}
