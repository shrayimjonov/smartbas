package uz.davrbank.educontrolservice.service.cases.course;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CourseEmployeeFilter;
import uz.davrbank.educontrolservice.feign.course.CourseEmployeeFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.course.CourseEmployeeInterface;

import java.util.List;

@Service
@Slf4j
public class CourseEmployeeService implements CourseEmployeeInterface {
    private final CourseEmployeeFeign courseEmployeeFeign;
    private final JwtTokenService jwtTokenService;

    public CourseEmployeeService(CourseEmployeeFeign courseEmployeeFeign, JwtTokenService jwtTokenService) {
        this.courseEmployeeFeign = courseEmployeeFeign;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Object startedCourse(Long courseId) {
        log.info("REQUEST (startedCourse) FROM [course-service]");
        String employeeId = jwtTokenService.getCurrentUserId();

        return courseEmployeeFeign.startedCourse(courseId, employeeId);

    }

    @Override
    public Object completedCourse(Long courseId) {
        log.info("REQUEST (completedCourse) FROM [course-service]");
        String employeeId = jwtTokenService.getCurrentUserId();

        return courseEmployeeFeign.completedCourse(courseId, employeeId);
    }

    @Override
    public Object getPageable(CourseEmployeeFilter filter) {
        log.info("REQUEST (getPageable) FROM [course-service]");
        CourseEmployeeFilter prepared = filter != null ? filter : new CourseEmployeeFilter();
        return courseEmployeeFeign.getPageable(prepared);
    }

    @Override
    public Object getList(CourseEmployeeFilter filter) {
        log.info("REQUEST (getList) FROM [course-service]");
        CourseEmployeeFilter prepared = filter != null ? filter : new CourseEmployeeFilter();
        return courseEmployeeFeign.getList(prepared);
    }

    @Override
    public List<Object> getEmployeeStartedCourses() {
        log.info("REQUEST (getEmployeeStartedCourses) FROM [course-service]");
        String employeeId = jwtTokenService.getCurrentUserId();

        return courseEmployeeFeign.attachedCourse(employeeId);
    }
}
