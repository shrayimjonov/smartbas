package uz.davrbank.educontrolservice.service.cases.course;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.course.CourseRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.filter.CourseFilter;
import uz.davrbank.educontrolservice.feign.course.CourseFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.course.CourseService;

import java.util.Set;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseFeign courseFeign;
    private final JwtTokenService jwtTokenService;

    public CourseServiceImpl(CourseFeign courseFeign, JwtTokenService jwtTokenService) {
        this.courseFeign = courseFeign;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Object create(CourseRequestDTO courseRequestDTO) {
        log.info("REQUEST (createCourse) FROM [course-service]");
//        String userId = jwtTokenService.getCurrentUserId();
        return courseFeign.createCourse(courseRequestDTO, "1");
    }

    @Override
    public Object update(Long id, CourseRequestDTO courseRequestDTO) {
        log.info("REQUEST (updateCourse) FROM [course-service] ");
        return courseFeign.updateCourse(id, courseRequestDTO);
    }

    @Override
    public Object getPageable(CourseFilter filter) {
        log.info("REQUEST (getPageable) FROM [course-service]");
        CourseFilter prepared = prepareCourseFilter(filter);
        return courseFeign.getPageable(prepared);
    }

    @Override
    public Object getList(CourseFilter filter) {
        log.info("REQUEST (getList) FROM [course-service]");
        CourseFilter prepared = prepareCourseFilter(filter);
        return courseFeign.getList(prepared);
    }

    @Override
    public Object get(Long id) {
        log.info("REQUEST (getCourseById) FROM [course-service]");
        return courseFeign.getCourseById(id);
    }

    @Override
    public Object delete(Long id) {
        log.info("REQUEST (deleteCourse) FROM [course-service]");
        return courseFeign.deleteCourse(id);
    }

    @Override
    public Object accessEmployees(Long courseId, Set<String> employeeIds) {
        log.info("REQUEST (accessEmployees) FROM [course-service]");
        return courseFeign.accessEmployees(courseId, employeeIds);
    }

    private CourseFilter prepareCourseFilter(CourseFilter filter) {
        CourseFilter prepared = filter != null ? filter : new CourseFilter();
        prepared.setEmployeeId(jwtTokenService.getCurrentUserId());
        prepared.setRoles(jwtTokenService.roles());
        return prepared;
    }
}
