package uz.davrbank.educontrolservice.service.dao.course;

import uz.davrbank.educontrolservice.aggregate.dto.assigment.course.CourseRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CourseFilter;

import java.util.Set;

public interface CourseService {

    Object create(CourseRequestDTO courseRequestDTO);

    Object update(Long id, CourseRequestDTO courseRequestDTO);

    Object getPageable(CourseFilter filter);

    Object getList(CourseFilter filter);

    Object get(Long id);

    Object delete(Long id);

    Object accessEmployees(Long courseId, Set<String> employeeIds);
}