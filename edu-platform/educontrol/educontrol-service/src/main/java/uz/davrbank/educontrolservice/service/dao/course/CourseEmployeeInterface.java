package uz.davrbank.educontrolservice.service.dao.course;

import uz.davrbank.educontrolservice.aggregate.dto.filter.CourseEmployeeFilter;

import java.util.List;

public interface CourseEmployeeInterface {

    Object startedCourse(Long courseId);

    Object completedCourse(Long courseId);

    Object getPageable(CourseEmployeeFilter filter);

    Object getList(CourseEmployeeFilter filter);

    List<Object> getEmployeeStartedCourses();
}
