package uz.davrbank.courseservice.service.dao.course;

import uz.davrbank.courseservice.aggregation.dto.course.CourseRequestDTO;
import uz.davrbank.courseservice.aggregation.dto.course.CourseResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.filter.CourseFilter;

import java.util.List;
import java.util.Set;


public interface CourseService {

    ResponseSuccess create(CourseRequestDTO courseRequestDTO, String userId);

    ResponseSuccess update(Long id, CourseRequestDTO courseRequestDTO);

    CourseResponseDTO get(Long id);

    ResponseSuccess delete(Long id);

    List<CourseResponseDTO> getList(CourseFilter filter);

    PageResponseDTO<CourseResponseDTO> getPageable(CourseFilter filter);

    void accessEmployees(Long courseId, Set<String> employeeIds);
}
