package uz.davrbank.courseservice.service.cases.course;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.davrbank.courseservice.aggregation.dto.course.UserCourseResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.enums.Status;
import uz.davrbank.courseservice.aggregation.filter.CourseEmployeeFilter;
import uz.davrbank.courseservice.aggregation.model.course.CourseEmployee;
import uz.davrbank.courseservice.repository.CurseEmployeeRepository;
import uz.davrbank.courseservice.repository.strategy.SpecificationApplyFilterStrategy;
import uz.davrbank.courseservice.service.dao.course.CourseEmployeeService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseEmployeeServiceImpl implements CourseEmployeeService {


    EntityManager entityManager;
    CurseEmployeeRepository repository;
    SpecificationApplyFilterStrategy<CourseEmployee, CourseEmployeeFilter> strategy;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void startedCourse(Long courseId, String employeeId) {
        log.info("Request to startedCourse courseId: {}, employeeId: {}", courseId, employeeId);
        CourseEmployee courseEmployee = repository.findByCourseIdAndEmployeeIdAndIsCompleted(courseId, employeeId, false);
        if (courseEmployee == null) {
            repository.save(new CourseEmployee(courseId, employeeId, false, Status.STARTED));
        } else {
            courseEmployee.setStartedDate(LocalDateTime.now());
            courseEmployee.setIsCompleted(false);
            courseEmployee.setStatus(Status.STARTED);
            repository.save(courseEmployee);
        }
    }

    @Override
    public void completedCourse(Long courseId, String employeeId) {
        log.info("Request to completedCourse courseId: {}, employeeId: {}", courseId, employeeId);
        CourseEmployee courseEmployee = repository.findByCourseIdAndEmployeeIdAndIsCompleted(courseId, employeeId, false);
        courseEmployee.setStatus(Status.COMPLETED);
        courseEmployee.setCompletedDate(LocalDateTime.now());
        courseEmployee.setIsCompleted(true);
        repository.save(courseEmployee);
    }

    @Override
    public PageResponseDTO<UserCourseResponseDTO> getPageable(CourseEmployeeFilter filter) {
        preparePageableFilter(filter);
        Page<CourseEmployee> page = repository.findAll(strategy.applyFilter(filter), strategy.buildPage(filter));
        List<CourseEmployee> content = page.getContent();
        Map<Long, String> courseNames = loadCourseNames(collectCourseIds(content));
        Map<String, String> employeeNames = loadEmployeeNames(collectEmployeeIds(content));

        List<UserCourseResponseDTO> users = content.stream()
                .map(courseEmployee -> toUserCourseResponseDTO(courseEmployee, courseNames, employeeNames))
                .toList();

        return new PageResponseDTO<>(
                users,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    public List<UserCourseResponseDTO> getList(CourseEmployeeFilter filter) {
        prepareListFilter(filter);
        List<CourseEmployee> courseEmployees = repository.findAll(strategy.applyFilter(filter), filter.getOrderedSortBy());
        if (courseEmployees.isEmpty()) {
            return List.of();
        }
        Map<Long, String> courseNames = loadCourseNames(collectCourseIds(courseEmployees));
        Map<String, String> employeeNames = loadEmployeeNames(collectEmployeeIds(courseEmployees));

        List<UserCourseResponseDTO> result = new ArrayList<>();
        for (CourseEmployee courseEmployee : courseEmployees) {
            result.add(toUserCourseResponseDTO(courseEmployee, courseNames, employeeNames));
        }
        return result;
    }

    private UserCourseResponseDTO toUserCourseResponseDTO(CourseEmployee courseEmployee,
                                                          Map<Long, String> courseNames,
                                                          Map<String, String> employeeNames) {
        return new UserCourseResponseDTO(
                courseEmployee.getEmployeeId(),
                courseEmployee.getCourseId(),
                courseNames.get(courseEmployee.getCourseId()),
                employeeNames.get(courseEmployee.getEmployeeId()),
                courseEmployee.getStatus() != null ? courseEmployee.getStatus().name() : null,
                formatDateTime(courseEmployee.getStartedDate()),
                formatDateTime(courseEmployee.getCompletedDate())
        );
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATE_TIME_FORMATTER);
    }

    private Map<Long, String> loadCourseNames(List<Long> courseIds) {
        if (courseIds.isEmpty()) {
            return Map.of();
        }
        String sql = "SELECT id, name FROM course WHERE id IN (:ids)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("ids", courseIds);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        Map<Long, String> result = new HashMap<>();
        for (Object[] row : rows) {
            result.put(((Number) row[0]).longValue(), (String) row[1]);
        }
        return result;
    }

    private Map<String, String> loadEmployeeNames(List<String> employeeIds) {
        if (employeeIds.isEmpty()) {
            return Map.of();
        }
        String sql = """
                SELECT employee_id, first_name, last_name
                FROM employee_ids
                WHERE employee_id IN (:ids)
                """;
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("ids", employeeIds);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        Map<String, String> result = new HashMap<>();
        for (Object[] row : rows) {
            String firstName = row[1] != null ? row[1].toString() : "";
            String lastName = row[2] != null ? row[2].toString() : "";
            String fullName = (firstName + " " + lastName).trim();
            result.put(row[0].toString(), fullName.isBlank() ? null : fullName);
        }
        return result;
    }

    private List<Long> collectCourseIds(List<CourseEmployee> courseEmployees) {
        return courseEmployees.stream()
                .map(CourseEmployee::getCourseId)
                .distinct()
                .toList();
    }

    private List<String> collectEmployeeIds(List<CourseEmployee> courseEmployees) {
        return courseEmployees.stream()
                .map(CourseEmployee::getEmployeeId)
                .distinct()
                .toList();
    }

    private void preparePageableFilter(CourseEmployeeFilter filter) {
        if (filter == null || filter.getCourseId() == null) {
            throw new IllegalArgumentException("courseId is required");
        }
        normalizeOrderBy(filter);
        validateSort(filter);
    }

    private void prepareListFilter(CourseEmployeeFilter filter) {
        if (filter == null || !StringUtils.hasText(filter.getEmployeeId())) {
            throw new IllegalArgumentException("employeeId is required");
        }
        if (filter.getStatuses() == null || filter.getStatuses().isEmpty()) {
            filter.setStatuses(List.of(Status.STARTED, Status.NOT_STARTED));
        }
        normalizeOrderBy(filter);
        validateSort(filter);
    }

    private void validateSort(CourseEmployeeFilter filter) {
        String orderBy = filter.getOrderBy();
        List<String> allowedSortFields = List.of(
                "id", "courseId", "employeeId", "status", "startedDate", "completedDate", "isCompleted", "score"
        );
        if (!allowedSortFields.contains(orderBy)) {
            throw new IllegalArgumentException("Недопустимое поле сортировки");
        }
        String sortOrder = filter.getSortOrder();
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            throw new IllegalArgumentException("Недопустимое направление сортировки");
        }
    }

    private void normalizeOrderBy(CourseEmployeeFilter filter) {
        String orderBy = filter.getOrderBy();
        if (!StringUtils.hasText(orderBy)) {
            return;
        }
        String lower = orderBy.toLowerCase();
        String normalized = switch (lower) {
            case "course_id", "courseid" -> "courseId";
            case "employee_id", "employeeid" -> "employeeId";
            case "started_date", "starteddate" -> "startedDate";
            case "completed_date", "completeddate" -> "completedDate";
            case "is_completed", "iscompleted" -> "isCompleted";
            case "id", "status", "score" -> lower;
            default -> orderBy;
        };
        filter.setOrderBy(normalized);
    }

    private String getCourseNameById(Long courseId) {
        try {
            String sql = "SELECT name FROM course WHERE id = :id";
            return (String) entityManager.createNativeQuery(sql)
                    .setParameter("id", courseId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
