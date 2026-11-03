package uz.davrbank.courseservice.service.cases.step;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.davrbank.courseservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.courseservice.aggregation.dto.step.AttachStepResponseDTO;
import uz.davrbank.courseservice.aggregation.enums.Status;
import uz.davrbank.courseservice.aggregation.filter.StepEmployeeFilter;
import uz.davrbank.courseservice.aggregation.model.step.StepEmployee;
import uz.davrbank.courseservice.repository.StepEmployeeRepository;
import uz.davrbank.courseservice.repository.strategy.SpecificationApplyFilterStrategy;
import uz.davrbank.courseservice.service.dao.step.StepEmployeeService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StepEmployeeServiceImpl implements StepEmployeeService {

    EntityManager entityManager;
    StepEmployeeRepository repository;
    SpecificationApplyFilterStrategy<StepEmployee, StepEmployeeFilter> strategy;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void startedStep(Long stepId, String employeeId) {
        log.info("Request to startedStep stepId: {}, employeeId: {}", stepId, employeeId);
        StepEmployee stepEmployee = repository.findByStepIdAndEmployeeIdAndIsCompleted(stepId, employeeId, false);
        if (stepEmployee == null) {
            repository.save(new StepEmployee(stepId, employeeId, false, Status.STARTED));
        } else {
            stepEmployee.setStatus(Status.STARTED);
            stepEmployee.setStartedDate(LocalDateTime.now());
            stepEmployee.setIsCompleted(false);
            repository.save(stepEmployee);
        }
    }

    @Override
    public void completedStep(Long stepId, String employeeId) {
        log.info("Request to completedStep stepId: {}, employeeId: {}", stepId, employeeId);
        StepEmployee stepEmployee = repository.findByStepIdAndEmployeeIdAndIsCompleted(stepId, employeeId, false);
        stepEmployee.setStatus(Status.COMPLETED);
        stepEmployee.setCompletedDate(LocalDateTime.now());
        stepEmployee.setIsCompleted(true);
        repository.save(stepEmployee);
    }

    @Override
    public PageResponseDTO<AttachStepResponseDTO> getPageable(StepEmployeeFilter filter) {
        StepEmployeeFilter safeFilter = filter != null ? filter : new StepEmployeeFilter();
        preparePageableFilter(safeFilter);
        Page<StepEmployee> page = repository.findAll(strategy.applyFilter(safeFilter), strategy.buildPage(safeFilter));
        List<StepEmployee> content = page.getContent();
        Map<Long, String> stepNames = loadStepNames(collectStepIds(content));
        Map<String, String> employeeNames = loadEmployeeNames(collectEmployeeIds(content));

        List<AttachStepResponseDTO> steps = content.stream()
                .map(stepEmployee -> toAttachStepResponseDTO(stepEmployee, stepNames, employeeNames))
                .toList();

        return new PageResponseDTO<>(
                steps,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    public List<AttachStepResponseDTO> getList(StepEmployeeFilter filter) {
        StepEmployeeFilter safeFilter = filter != null ? filter : new StepEmployeeFilter();
        prepareListFilter(safeFilter);
        List<StepEmployee> steps = repository.findAll(strategy.applyFilter(safeFilter), safeFilter.getOrderedSortBy());
        if (steps.isEmpty()) {
            return List.of();
        }
        Map<Long, String> stepNames = loadStepNames(collectStepIds(steps));
        Map<String, String> employeeNames = loadEmployeeNames(collectEmployeeIds(steps));
        return steps.stream()
                .map(stepEmployee -> toAttachStepResponseDTO(stepEmployee, stepNames, employeeNames))
                .toList();
    }

    private AttachStepResponseDTO toAttachStepResponseDTO(StepEmployee stepEmployee,
                                                          Map<Long, String> stepNames,
                                                          Map<String, String> employeeNames) {
        return new AttachStepResponseDTO(
                stepEmployee.getEmployeeId(),
                stepEmployee.getStepId(),
                stepNames.get(stepEmployee.getStepId()),
                employeeNames.get(stepEmployee.getEmployeeId()),
                stepEmployee.getIsCompleted(),
                stepEmployee.getStatus() != null ? stepEmployee.getStatus().name() : null,
                formatDateTime(stepEmployee.getStartedDate()),
                formatDateTime(stepEmployee.getCompletedDate())
        );
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATE_TIME_FORMATTER);
    }

    private Map<Long, String> loadStepNames(List<Long> stepIds) {
        if (stepIds.isEmpty()) {
            return Map.of();
        }
        String sql = "SELECT id, name FROM step WHERE id IN (:ids)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("ids", stepIds);
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

    private List<Long> collectStepIds(List<StepEmployee> stepEmployees) {
        return stepEmployees.stream()
                .map(StepEmployee::getStepId)
                .distinct()
                .toList();
    }

    private List<String> collectEmployeeIds(List<StepEmployee> stepEmployees) {
        return stepEmployees.stream()
                .map(StepEmployee::getEmployeeId)
                .distinct()
                .toList();
    }

    private void preparePageableFilter(StepEmployeeFilter filter) {
        if (filter == null || filter.getStepId() == null) {
            throw new IllegalArgumentException("stepId is required");
        }
        normalizeOrderBy(filter);
        validateSort(filter);
    }

    private void prepareListFilter(StepEmployeeFilter filter) {
        if (!StringUtils.hasText(filter.getEmployeeId())) {
            throw new IllegalArgumentException("employeeId is required");
        }
        if (filter.getStatuses() == null || filter.getStatuses().isEmpty()) {
            filter.setStatuses(List.of(Status.STARTED, Status.NOT_STARTED));
        }
        normalizeOrderBy(filter);
        validateSort(filter);
    }

    private void validateSort(StepEmployeeFilter filter) {
        String orderBy = filter.getOrderBy();
        List<String> allowedSortFields = List.of(
                "id", "stepId", "employeeId", "status", "startedDate", "completedDate", "isCompleted", "score"
        );
        if (!allowedSortFields.contains(orderBy)) {
            throw new IllegalArgumentException("Недопустимое поле сортировки");
        }
        String sortOrder = filter.getSortOrder();
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            throw new IllegalArgumentException("Недопустимое направление сортировки");
        }
    }

    private void normalizeOrderBy(StepEmployeeFilter filter) {
        String orderBy = filter.getOrderBy();
        if (!StringUtils.hasText(orderBy)) {
            return;
        }
        String lower = orderBy.toLowerCase();
        String normalized = switch (lower) {
            case "step_id", "stepid" -> "stepId";
            case "employee_id", "employeeid" -> "employeeId";
            case "started_date", "starteddate" -> "startedDate";
            case "completed_date", "completeddate" -> "completedDate";
            case "is_completed", "iscompleted" -> "isCompleted";
            case "id", "status", "score" -> lower;
            default -> orderBy;
        };
        filter.setOrderBy(normalized);
    }
}
