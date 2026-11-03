package uz.davrbank.courseservice.service.dao.course;

import uz.davrbank.courseservice.aggregation.dto.course.AttachCourseResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.course.UserCourseResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.filter.CourseEmployeeFilter;

import java.util.List;

public interface CourseEmployeeService {

    void startedCourse(Long courseId, String employeeId);

    void completedCourse(Long courseId, String employeeId);

    PageResponseDTO<UserCourseResponseDTO> getPageable(CourseEmployeeFilter filter);

    List<UserCourseResponseDTO> getList(CourseEmployeeFilter filter);
}
