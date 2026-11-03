package uz.davrbank.educontrolservice.service.cases.employee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.employee.EmployeeDepartmentRequestDTO;
import uz.davrbank.educontrolservice.feign.EmployeeClientFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.employee.EmployeeInterface;

import java.util.List;

@Service
@Slf4j
public class EmployeeService implements EmployeeInterface {
    private final EmployeeClientFeign employeeClientFeign;
    private final JwtTokenService jwtTokenService;

    public EmployeeService(EmployeeClientFeign employeeClientFeign, JwtTokenService jwtTokenService1) {
        this.employeeClientFeign = employeeClientFeign;
        this.jwtTokenService = jwtTokenService1;
    }



    @Override
    public Object getAllEmployees(int page, int size) {
        log.info("REQUEST (getAllEmployees) FROM [employee-service]");
        log.info(jwtTokenService.roles().toString());
        log.info(jwtTokenService.getCurrentUserId());
        String employeeId = jwtTokenService.getCurrentUserId();
        List<String> roles = jwtTokenService.roles();
        return employeeClientFeign.getAllEmployees(page, size, employeeId, roles);
    }
    @Override
    public Object getOneEmployee(String id) {
        log.info("REQUEST (getOneEmployee) FROM [employee-service]");
        return employeeClientFeign.getOneEmployee(id);
    }

    @Override
    public Object mergeEmployeeDepartment(String employeeId, Long departmentId, Long positionId) {
        log.info("REQUEST (mergeEmployeeDepartment) FROM [employee-service]");
        log.info(jwtTokenService.getCurrentUsername());
        EmployeeDepartmentRequestDTO dto  = new EmployeeDepartmentRequestDTO(
                employeeId,
                departmentId,
                positionId
        );
        return employeeClientFeign.mergeEmployeeDepartment(dto);
    }

    @Override
    public Object updateMergedEmployeeDepartment(Long departmentId, Long positionId) {
        log.info("REQUEST (updateMergedEmployeeDepartment) FROM [employee-service]");
        EmployeeDepartmentRequestDTO requestDTO = new EmployeeDepartmentRequestDTO(
                jwtTokenService.getCurrentUserId(),
                departmentId,
                positionId
        );
        return employeeClientFeign.updateMergedEmployeeDepartment(requestDTO);
    }

    @Override
    public String testEmployee() {
        log.info("REQUEST (testEmployee) FROM [employee-service]");
        return employeeClientFeign.testEmployee();
    }

    @Override
    public List<Object> getAllEmployeeByDepartmentId(Long departmentId) {
        log.info("REQUEST (getAllEmployeeByDepartmentId) FROM [employee-service]");
        return employeeClientFeign.getEmployeeByDepartment(departmentId);
    }

    @Override
    public List<Object> getAllRoles() {
        log.info("REQUEST (getAllRoles) FROM [employee-service]");
        return employeeClientFeign.getAllRoles();
    }

    @Override
    public Object getAllEmployeeNotInDepartment(int page, int size, String sort) {
        log.info("REQUEST (getAllEmployeeNotInDepartment) FROM [employee-service]");
        return employeeClientFeign.getEmployeeNotInDepartment(page, size, sort);
    }

    @Override
    public Boolean checkEmployeeInDepartment(String username) {
        log.info("REQUEST (checkEmployeeInDepartment) FROM [employee-service]");
        return employeeClientFeign.checkEmployeeInDepartment(username);
    }

    @Override
    public Object getAllFromDB() {
        log.info("REQUEST (getAllFromDB) FROM [employee-service]");
        return employeeClientFeign.getAllFromDB();
    }

    @Override
    public List<String> getEmployeeIdsByDepartment(Long departmentId) {
        return employeeClientFeign.getEmployeeIdsByDepartment(departmentId);
    }
}