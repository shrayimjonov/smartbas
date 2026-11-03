package uz.davrbank.assignmentservice.service.cases.task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.task.*;
import uz.davrbank.assignmentservice.aggregation.exception.condition.DataNotFoundException;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class TaskDBService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskDBService.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final EntityManager entityManager;

    public TaskDBService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EmployeeDepartmentDTO getEmployeeIdAndDepartmentId(List<String> employeeIds) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return new EmployeeDepartmentDTO(Collections.emptyList(), Collections.emptyList());
        }

        // Динамическая IN-клауза
        String inClause = employeeIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        // SQL-запрос должен возвращать и employee_id, и department_id
        String query = """
                SELECT e.employee_id, ed.department_id 
                FROM employee_department ed
                JOIN employee_ids e ON ed.id = e.employee_ref
                WHERE e.employee_id IN (""" + inClause + ")";

        Query nativeQuery = entityManager.createNativeQuery(query);

        // Устанавливаем параметры
        for (int i = 0; i < employeeIds.size(); i++) {
            nativeQuery.setParameter(i + 1, employeeIds.get(i));
        }

        List<Object[]> results = nativeQuery.getResultList();

        // Разделяем данные на два списка
        List<String> resultEmployeeIds = new ArrayList<>();
        List<Long> departmentIds = new ArrayList<>();

        for (Object[] row : results) {
            resultEmployeeIds.add((String) row[0]); // employee_id
            departmentIds.add(((Number) row[1]).longValue()); // department_id
        }

        return new EmployeeDepartmentDTO(resultEmployeeIds, departmentIds);
    }

    public PageResponseDTO<TaskResponseDTO> getAllTasks(Pageable pageable) {

        String baseSql = """
                    SELECT
                        e.taskuuid::text AS task_id,
                        ed.first_name || ' ' || ed.last_name AS full_name,
                        t.name AS task_name,
                        TO_CHAR(t.start_date, 'DD-MM-YYYY') AS start_date,
                        TO_CHAR(t.end_date, 'DD-MM-YYYY') AS end_date,
                        t.description,
                        t.score,
                        e.priority,
                        e.status,
                        TO_CHAR(e.completed_date, 'DD-MM-YYYY') AS completed_date,
                        CASE
                            WHEN t.end_date IS NULL THEN NULL
                            WHEN e.status = 'COMPLETED' AND e.completed_date::date <= t.end_date::date THEN 'Не просрочено'
                            WHEN e.status = 'COMPLETED' AND e.completed_date::date > t.end_date::date
                                THEN CONCAT('Просрочено на ', e.completed_date::date - t.end_date::date, ' дней, но выполнено')
                            WHEN e.status != 'COMPLETED' AND CURRENT_DATE > t.end_date::date
                                THEN CONCAT('Просрочено на ', CURRENT_DATE - t.end_date::date, ' дней')
                            WHEN e.status != 'COMPLETED' AND CURRENT_DATE < t.end_date::date
                                THEN CONCAT('Осталось ', t.end_date::date - CURRENT_DATE, ' дней')
                            WHEN CURRENT_DATE = t.end_date::date THEN 'Сегодня последний день'
                            ELSE NULL
                            END AS expired_status
                    FROM task_employee e
                             LEFT JOIN task t ON e.task_id = t.id
                            LEFT JOIN  employee_ids ed ON e.employee_id = ed.employee_id
                """;
        List<String> allowedSortFields = List.of("t.id", "t.name", "t.start_date", "t.end_date", "t.score", "e.priority", "e.status");
        String sortField = "t.id"; // default
        String sortDirection = "DESC"; // default

        if (pageable.getSort().isSorted()) {
            Sort.Order order = pageable.getSort().iterator().next();
            String requestedField = "t." + order.getProperty();
            if (allowedSortFields.contains(requestedField)) {
                sortField = requestedField;
                sortDirection = order.getDirection().isAscending() ? "ASC" : "DESC";
            }
        }
        String finalSql = baseSql +
                " ORDER BY " + sortField + " " + sortDirection +
                " LIMIT :limit OFFSET :offset";


        List<TaskResponseDTO> taskDTOs = entityManager
                .createNativeQuery(finalSql, TaskResponseDTO.class)
                .setParameter("limit", pageable.getPageSize())
                .setParameter("offset", pageable.getPageSize() * pageable.getPageNumber())
                .getResultList();


        String countSql = """
                    SELECT COUNT(*)
                    FROM task_employee e
                             LEFT JOIN task t ON e.task_id = t.id
                """;
        Long totalElements = ((Number) entityManager.createNativeQuery(countSql)
                .getSingleResult()).longValue();

        // Получаем общее количество страниц
        int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());

        return new PageResponseDTO<>(
                taskDTOs,
                totalElements,
                totalPages,
                pageable.getPageNumber(),
                taskDTOs.size()
        );
    }

    @Transactional
    public void changeStatusAndDateTaskEmployee(String employeeId, Long taskId) {

        String sql = """
                 UPDATE task_employee
                 SET
                     is_completed = CASE WHEN ? = 'COMPLETED' THEN true ELSE false END,
                     completed_date = CASE WHEN ? = 'COMPLETED' THEN NOW() ELSE NULL END
                 WHERE employee_id = ? AND task_id = ?
                """;

        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setParameter(1, employeeId);
        nativeQuery.setParameter(2, taskId);
        nativeQuery.executeUpdate();
    }


    /**
     * Retrieves a paginated list of tasks associated with a specific employee ID.
     *
     * @param employeeId the ID of the employee whose tasks are to be retrieved
     * @param page       the page number to retrieve
     * @param size       the number of tasks per page
     * @return a PageResponseDTO containing a list of TaskResponseEmployeeDepartmentDTO,
     * total number of elements, total pages, current page, and page size
     */
    public PageResponseDTO<TaskResponseEmployeeDepartmentDTO> getByEmployeeId(String employeeId, int page, int size) {

        String sql = """
                SELECT
                    t.id,
                    t.name,
                    TO_CHAR(t.start_date, 'DD-MM-YYYY') AS startDate,
                    TO_CHAR(t.end_date, 'DD-MM-YYYY') AS endDate,
                    t.description,
                    e.status,
                    e.priority
                FROM task_employee e
                         LEFT JOIN task t ON e.task_id = t.id
                WHERE e.employee_id = :employeeId
                LIMIT :limit OFFSET :offset
                
                """;

        String countSql = """
                    SELECT COUNT(DISTINCT t.id)
                    FROM task t
                    JOIN task_employee e ON e.task_id = t.id
                    WHERE e.employee_id = :employeeId
                """;

        // Получаем общее количество записей
        Long totalElements = ((Number) entityManager.createNativeQuery(countSql)
                .setParameter("employeeId", employeeId)
                .getSingleResult()).longValue();

        // Выполняем запрос с пагинацией
        List<TaskResponseEmployeeDepartmentDTO> results = entityManager.createNativeQuery(sql, TaskResponseEmployeeDepartmentDTO.class)
                .setParameter("employeeId", employeeId)
                .setParameter("limit", size)
                .setParameter("offset", page * size)
                .getResultList();


        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PageResponseDTO<>(
                results,
                totalElements,
                totalPages,
                page,
                results.size()
        );
    }

    List<String> getEmployeeIdByDepartmentId(Long departmentId) {
        String query = """
                SELECT  e.employee_id
                FROM employee_ids e
                JOIN employee_department ed ON e.employee_ref = ed.id
                WHERE ed.department_id = :departmentId
                """;

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("departmentId", departmentId);

        return nativeQuery.getResultList();
    }

    public PageResponseDTO<TaskResponseDTO> getAllTaskByDepartmentId(String userId, int page, int size) {
        String sqlDepartmentId = """
                SELECT ed.department_id
                FROM employee_department ed
                LEFT JOIN public.employee_ids ei on ed.id = ei.employee_ref
                WHERE ei.employee_id =:employeeId;
                """;
        Query nativeQueryDepartmentId = entityManager.createNativeQuery(sqlDepartmentId);
        nativeQueryDepartmentId.setParameter("employeeId", userId);
        Long departmentId = ((Number) nativeQueryDepartmentId.getSingleResult()).longValue();

        String employees = """
                SELECT
                    e.taskuuid::text AS task_id,
                    ed.first_name || ' ' || ed.last_name AS full_name,
                    t.name AS task_name,
                    TO_CHAR(t.start_date, 'DD-MM-YYYY') AS start_date,
                    TO_CHAR(t.end_date, 'DD-MM-YYYY') AS end_date,
                    t.description,
                    t.score,
                    e.priority,
                    e.status,
                    TO_CHAR(e.completed_date, 'DD-MM-YYYY') AS completed_date,
                    CASE
                        WHEN t.end_date IS NULL THEN NULL
                        WHEN e.status = 'COMPLETED' AND e.completed_date::date <= t.end_date::date THEN 'Не просрочено'
                        WHEN e.status = 'COMPLETED' AND e.completed_date::date > t.end_date::date
                            THEN CONCAT('Просрочено на ', e.completed_date::date - t.end_date::date, ' дней, но выполнено')
                        WHEN e.status != 'COMPLETED' AND CURRENT_DATE > t.end_date::date
                            THEN CONCAT('Просрочено на ', CURRENT_DATE - t.end_date::date, ' дней')
                        WHEN e.status != 'COMPLETED' AND CURRENT_DATE < t.end_date::date
                            THEN CONCAT('Осталось ', t.end_date::date - CURRENT_DATE, ' дней')
                        WHEN CURRENT_DATE = t.end_date::date THEN 'Сегодня последний день'
                        ELSE NULL
                        END AS expired_status
                FROM task_employee e
                         LEFT JOIN task t ON e.task_id = t.id
                         LEFT JOIN  employee_ids ed ON e.employee_id = ed.employee_id
                WHERE e.department_id =:departmentId
                LIMIT :limit OFFSET :offset
                """;
        Query nativeQuery = entityManager.createNativeQuery(employees, TaskResponseDTO.class);
        nativeQuery.setParameter("departmentId", departmentId);
        nativeQuery.setParameter("limit", size);
        nativeQuery.setParameter("offset", page * size);
        List<TaskResponseDTO> results = nativeQuery.getResultList();


        int totalPages = (int) Math.ceil((double) results.size() / size);

        return new PageResponseDTO<>(
                results,
                results.size(),
                totalPages,
                page,
                results.size()
        );
    }

    public void getOneTaskByTaskIdAndEmployeeId(String employeeId, Long taskId) {
        String query = """
                SELECT * FROM task_employee WHERE employee_id = ?1 AND task_id = ?2;
                """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, employeeId);
        nativeQuery.setParameter(2, taskId);
        List<?> result = nativeQuery.getResultList();
        if (result.isEmpty()) {
            throw new DataNotFoundException("Задача не найдена для сотрудника= " + employeeId + " и taskId= " + taskId);
        }
    }

    public TaskStatusResponseDTO getTasksByStatus(String employeeId, String taskStatus, int page, int size) {
        try {
            // SQL для подсчёта всех задач по статусам
            String statusCountSql = """
                        SELECT e.status, COUNT(t.id)
                        FROM task t
                        JOIN task_employee e ON e.task_id = t.id
                        WHERE e.employee_id = :employeeId
                        GROUP BY e.status
                    """;

            // SQL для подсчёта невыполненных задач
            String incompleteCountSql = """
                        SELECT COUNT(t.id)
                        FROM task t
                        JOIN task_employee e ON e.task_id = t.id
                        WHERE e.employee_id = :employeeId
                          AND e.status != 'COMPLETED'
                    """;

            // SQL для получения задач по статусу
            String sql = """
                        SELECT
                            t.id AS task_id,
                            t.name AS task_name,
                            t.start_date,
                            t.end_date,
                            t.description,
                            t.priority,
                            e.status,
                            COALESCE(
                                (SELECT ARRAY_AGG(DISTINCT f.name)
                                 FROM task_resource r
                                 JOIN files f ON f.id = r.resource_id
                                 WHERE r.task_id = t.id),
                                ARRAY[]::TEXT[]
                            ) AS file_names
                        FROM task t
                        JOIN task_employee e ON e.task_id = t.id
                        WHERE e.employee_id = :employeeId AND e.status = :taskStatus
                        GROUP BY t.id, t.name, t.start_date, t.end_date, t.description, t.priority, e.status
                        LIMIT :limit OFFSET :offset
                    """;

            // Получаем счётчик невыполненных
            Long incompleteCount = ((Number) entityManager.createNativeQuery(incompleteCountSql)
                    .setParameter("employeeId", employeeId)
                    .getSingleResult()).longValue();

            // Получаем счётчики по всем статусам
            List<Object[]> statusCountsRaw = entityManager.createNativeQuery(statusCountSql)
                    .setParameter("fullName", employeeId)
                    .getResultList();

            Map<String, Integer> statusCounts = new HashMap<>();
            for (Object[] row : statusCountsRaw) {
                String status = row[0].toString();
                int count = ((Number) row[1]).intValue();
                statusCounts.put(status, count);
            }

            // Опционально: добавим нули для отсутствующих статусов
            List<String> allStatuses = List.of("CREATED", "IN_PROGRESS", "TEST", "REJECTED", "COMPLETED");
            for (String s : allStatuses) {
                statusCounts.putIfAbsent(s, 0);
            }

            // Получаем задачи по нужному статусу
            List<Object[]> results = entityManager.createNativeQuery(sql)
                    .setParameter("employeeId", employeeId)
                    .setParameter("taskStatus", taskStatus)
                    .setParameter("limit", size)
                    .setParameter("offset", page * size)
                    .getResultList();

            List<TaskResponseEmployeeDepartmentDTO> content = results.stream().map(row -> {
                Long taskId = ((Number) row[0]).longValue();
                String taskName = (String) row[1];
                LocalDate startDate = row[2] != null ? ((Date) row[2]).toLocalDate() : null;
                LocalDate endDate = row[3] != null ? ((Date) row[3]).toLocalDate() : null;
                String description = (String) row[4];
                String priority = row[5] != null ? row[5].toString() : null;
                String status = row[6] != null ? row[6].toString() : null;

                List<ResourceDTO> resourceNames = new ArrayList<>();
                if (row[7] instanceof String[]) {
                    resourceNames = Arrays.stream((String[]) row[7])
                            .map(ResourceDTO::new)
                            .collect(Collectors.toList());
                } else if (row[7] instanceof Object[]) {
                    resourceNames = Arrays.stream((Object[]) row[7])
                            .map(obj -> new ResourceDTO(obj.toString()))
                            .collect(Collectors.toList());
                }

                return new TaskResponseEmployeeDepartmentDTO(
                        taskId,
                        taskName,
                        startDate != null ? startDate.format(formatter) : null,
                        endDate != null ? endDate.format(formatter) : null,
                        description,
                        status,
                        priority
                );
            }).collect(Collectors.toList());

            return new TaskStatusResponseDTO(incompleteCount.intValue(), content, statusCounts);

        } catch (Exception e) {
            LOGGER.error("Ошибка при получении задач по статусу: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении задач по статусу: " + e.getMessage());
        }
    }

    public TaskResponseDTO getTaskById(Long taskId, String userId) {
        String sql = """
                SELECT
                    CAST(t.id AS TEXT) AS task_id,
                    e.employee_id,
                    t.name AS task_name,
                    TO_CHAR(t.start_date, 'DD-MM-YYYY') AS start_date,
                    TO_CHAR(t.end_date, 'DD-MM-YYYY') AS end_date,
                    t.description,
                    t.score,
                    e.priority,
                    e.status,
                    TO_CHAR(e.completed_date, 'DD-MM-YYYY') AS completed_date,
                    CASE
                        WHEN t.end_date IS NULL THEN NULL
                        WHEN e.status = 'COMPLETED' AND e.completed_date::date <= t.end_date::date THEN 'Не просрочено'
                        WHEN e.status = 'COMPLETED' AND e.completed_date::date > t.end_date::date
                            THEN CONCAT('Просрочено на ', e.completed_date::date - t.end_date::date, ' дней, но выполнено')
                        WHEN e.status != 'COMPLETED' AND CURRENT_DATE > t.end_date::date
                            THEN CONCAT('Просрочено на ', CURRENT_DATE - t.end_date::date, ' дней')
                        WHEN e.status != 'COMPLETED' AND CURRENT_DATE < t.end_date::date
                            THEN CONCAT('Осталось ', t.end_date::date - CURRENT_DATE, ' дней')
                        WHEN CURRENT_DATE = t.end_date::date THEN 'Сегодня последний день'
                        ELSE NULL
                        END AS expired_status
                FROM task_employee e
                         LEFT JOIN task t ON e.task_id = t.id
                WHERE e.task_id = :taskId AND e.employee_id = :employeeUUID;
                """;
        TaskResponseDTO taskResponseDTO = (TaskResponseDTO) entityManager.createNativeQuery(sql, TaskResponseDTO.class)
                .setParameter("taskId", taskId)
                .setParameter("employeeUUID", userId)
                .getSingleResult();
        return taskResponseDTO;
    }
}
