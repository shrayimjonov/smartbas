package uz.davrbank.courseservice.service.cases.course;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uz.davrbank.courseservice.aggregation.dto.course.CourseRequestDTO;
import uz.davrbank.courseservice.aggregation.dto.course.CourseResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.courseservice.aggregation.dto.step.StepRequestDTO;
import uz.davrbank.courseservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.courseservice.aggregation.filter.CourseFilter;
import uz.davrbank.courseservice.aggregation.mapper.CourseMapper;
import uz.davrbank.courseservice.aggregation.model.CourseEmployeeAccess;
import uz.davrbank.courseservice.aggregation.model.course.Course;
import uz.davrbank.courseservice.aggregation.model.step.Step;
import uz.davrbank.courseservice.repository.CourseRepository;
import uz.davrbank.courseservice.repository.CurseEmployeeAccessRepository;
import uz.davrbank.courseservice.repository.StepRepository;
import uz.davrbank.courseservice.repository.strategy.SpecificationApplyFilterStrategy;
import uz.davrbank.courseservice.service.dao.course.CourseService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    CourseMapper courseMapper;
    EntityManager entityManager;
    StepRepository stepRepository;
    CourseRepository courseRepository;
    CurseEmployeeAccessRepository curseEmployeeAccessRepository;
    SpecificationApplyFilterStrategy<Course, CourseFilter> strategy;

    @Override
    @Transactional(noRollbackFor = {Exception.class, RuntimeException.class})
    public ResponseSuccess create(CourseRequestDTO request, String userId) {
        log.info("Создание курса: {}", request);
        try {
            Course course = courseMapper.toEntityFromReq(request);
            String sql = """
                    SELECT ed.department_id
                    FROM employee_department ed
                             LEFT JOIN employee_ids ei ON ed.id = ei.employee_ref
                    WHERE ei.employee_id = :employee_id
                    """;
            Query query = entityManager.createNativeQuery(sql, Long.class);
            query.setParameter("employee_id", userId);
            Long departmentId = (Long) query.getSingleResult();
            course.setDepartmentId(departmentId);
            course = courseRepository.save(course);
            if (request.steps() != null) {
                for (StepRequestDTO stepDto : request.steps()) {
                    try {
                        Step step = new Step(stepDto.name());
                        step.setCourse(course);
                        stepRepository.save(step);
                    } catch (Exception e) {
                        log.error("Error creating step: {}", e.getMessage());
                    }
                }
            }

            return new ResponseSuccess("Курс успешно создан");
        } catch (Exception e) {
            log.error("Ошибка при создании курса: {}", e.getMessage());
            throw new RuntimeException("Error while creating course: " + e.getMessage());
        }
    }

    @Override
    @Transactional(noRollbackFor = {Exception.class, RuntimeException.class})
    public ResponseSuccess update(Long id, CourseRequestDTO courseRequestDTO) {
        log.info("Обновление курса: {}", courseRequestDTO);
        try {
            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Course not found"));
            courseMapper.updateFromReqDto(courseRequestDTO, course);
            courseRepository.save(course);
            return new ResponseSuccess("Курс успешно обновлен");
        } catch (DataNotFoundException e) {
            log.error("Ошибка при обновлении курса: {}", e.getMessage());
            throw new DataNotFoundException("Ошибка при обновлении курса: " + e.getMessage());
        }
    }

    @Override
    public CourseResponseDTO get(Long id) {
        log.info("Получение курса по ID: {}", id);
        try {
            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Курс не найден"));
            return courseMapper.toDto(course);
        } catch (DataNotFoundException e) {
            log.error("Ошибка при получении курса: {}", e.getMessage());
            throw new DataNotFoundException("Ошибка при получении курса: " + e.getMessage());
        }
    }

    @Override
    public ResponseSuccess delete(Long id) {
        log.info("Удаление курса по ID: {}", id);
        try {
//            Course course = courseRepository.findById(id)
//                    .orElseThrow(() -> new DataNotFoundException("Курс не найден"));
//            course.setDeleted(true);
//            courseRepository.save(course);
            courseRepository.deleteById(id);
            return new ResponseSuccess("Курс успешно удален");
        } catch (DataNotFoundException e) {
            log.error("Ошибка при удалении курса: {}", e.getMessage());
            throw new DataNotFoundException("Ошибка при удалении курса: " + e.getMessage());
        }
    }

    @Override
    public List<CourseResponseDTO> getList(CourseFilter filter) {
        log.info("Получение всех курсов");
        prepareFilter(filter);
        List<Course> list = courseRepository.findAll(strategy.applyFilter(filter), filter.getOrderedSortBy());
        return courseMapper.toDtoList(list);
    }

    @Override
    public PageResponseDTO<CourseResponseDTO> getPageable(CourseFilter filter) {
        log.info("Получение курсов по фильтру");
        prepareFilter(filter);
        Page<Course> page = courseRepository.findAll(strategy.applyFilter(filter), strategy.buildPage(filter));
        List<CourseResponseDTO> content = page.getContent().stream()
                .map(courseMapper::toDto)
                .toList();
        return new PageResponseDTO<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }




    @Override
    @Transactional
    public void accessEmployees(Long courseId, Set<String> employeeIds) {
        courseRepository.findById(courseId).orElseThrow(() -> new DataNotFoundException("Course not found!"));
        try {
            curseEmployeeAccessRepository.deleteByCourseIdAndEmployeeId(courseId, employeeIds.stream().toList());
            List<CourseEmployeeAccess> courseEmployeeAccesses = employeeIds.stream()
                    .map(employeeId -> new CourseEmployeeAccess(employeeId, courseId)).toList();
            curseEmployeeAccessRepository.saveAll(courseEmployeeAccesses);
        } catch (Exception e) {
            log.error("Course not found: {}", e.getMessage());
            throw new DataNotFoundException("Course not found: " + e.getMessage());
        }
    }

    private void prepareFilter(CourseFilter filter) {
        if (filter == null) {
            return;
        }
        normalizeOrderBy(filter);
        validateSort(filter);
        applyRoleScope(filter);
    }

    private void applyRoleScope(CourseFilter filter) {
        List<String> roles = filter.getRoles() != null ? filter.getRoles() : List.of();
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("roles is required");
        }
        if (roles.contains("ROLE_SUPERADMIN")) {
            return;
        }
        if (roles.contains("ROLE_EMPLOYEE") || roles.contains("ROLE_DAVR")) {
            if (!StringUtils.hasText(filter.getEmployeeId())) {
                throw new IllegalArgumentException("employeeId is required");
            }
            filter.setDepartmentId(resolveDepartmentId(filter.getEmployeeId()));
            return;
        }
        throw new RuntimeException("Роль не поддерживается");
    }

    private void validateSort(CourseFilter filter) {
        String orderBy = filter.getOrderBy();
        Set<String> allowedSortFields = Set.of("id", "name", "timeComplete", "categoryId", "departmentId", "score");
        if (!allowedSortFields.contains(orderBy)) {
            throw new IllegalArgumentException("Недопустимое поле сортировки");
        }
        String sortOrder = filter.getSortOrder();
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            throw new IllegalArgumentException("Недопустимое направление сортировки");
        }
    }

    private void normalizeOrderBy(CourseFilter filter) {
        String orderBy = filter.getOrderBy();
        if (!StringUtils.hasText(orderBy)) {
            return;
        }
        String lower = orderBy.toLowerCase();
        String normalized = switch (lower) {
            case "time_complete", "timecomplete" -> "timeComplete";
            case "category_id", "categoryid" -> "categoryId";
            case "department_id", "departmentid" -> "departmentId";
            case "id", "name", "score" -> lower;
            default -> orderBy;
        };
        filter.setOrderBy(normalized);
    }

    private Long resolveDepartmentId(String employeeId) {
        String sql = """
                SELECT ed.department_id
                FROM employee_department ed
                         LEFT JOIN employee_ids ei ON ed.id = ei.employee_ref
                WHERE ei.employee_id = :employee_id
                """;
        Query query = entityManager.createNativeQuery(sql, Long.class);
        query.setParameter("employee_id", employeeId);
        return (Long) query.getSingleResult();
    }
}
