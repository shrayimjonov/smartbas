package uz.davrbank.educontrolservice.service.cases.department;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.department.DepartmentRequestDTO;
import uz.davrbank.educontrolservice.feign.department.DepartmentClientFeign;
import uz.davrbank.educontrolservice.service.dao.department.DepartmentInterface;

import java.util.List;

@Service
@Slf4j
public class DepartmentService implements DepartmentInterface {
    private final DepartmentClientFeign departmentClientFeign;

    public DepartmentService(DepartmentClientFeign departmentClientFeign) {
        this.departmentClientFeign = departmentClientFeign;
    }

    @Override
    public Object createDepartment(DepartmentRequestDTO departmentRequestDTO) {
        log.info("REQUEST (createDepartment) FROM [department-service]");
        return departmentClientFeign.createDepartment(departmentRequestDTO);
    }

    @Override
    public Object getAllDepartments(int page, int size, String search) {
        log.info("REQUEST (getAllDepartments) FROM [department-service]");
        return departmentClientFeign.getAllDepartments(page, size, search);
    }

    @Override
    public Object getOneDepartment(Long id) {
        log.info("REQUEST (getOneDepartment) FROM [department-service]");
        return departmentClientFeign.getOneDepartment(id);
    }

    @Override
    public Object deleteDepartment(Long id) {
        log.info("REQUEST (deleteDepartment) FROM [department-service]");
        return departmentClientFeign.deleteDepartment(id);
    }

    @Override
    public Object updateDepartment(Long id, DepartmentRequestDTO departmentRequestDTO) {
        log.info("REQUEST (updateDepartment) FROM [department-service]");
        return departmentClientFeign.updateDepartment(id, departmentRequestDTO);
    }

    @Override
    public List<Object> getAllDepartmentWithoutPagination() {
        log.info("REQUEST (getAllDepartmentWithoutPagination) FROM [department-service");
        return departmentClientFeign.getAllDepartmentWithoutPagination();
    }

    @Override
    public String testDepartment() {
        log.info("REQUEST (testDepartment) FROM [department-service]");
      return departmentClientFeign.testDepartment();
    }
}
