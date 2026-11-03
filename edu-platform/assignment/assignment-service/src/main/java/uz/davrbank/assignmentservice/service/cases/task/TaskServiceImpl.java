package uz.davrbank.assignmentservice.service.cases.task;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.assignmentservice.aggregation.dto.task.*;
import uz.davrbank.assignmentservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.assignmentservice.aggregation.filter.TaskFilter;
import uz.davrbank.assignmentservice.aggregation.model.task.Task;
import uz.davrbank.assignmentservice.repository.TaskRepository;
import uz.davrbank.assignmentservice.service.dao.TaskService;

import java.util.List;

/**
 * Класс TaskService реализует интерфейс TaskInterface и предоставляет
 *
 * @version 1.0
 */

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskDBService taskDBService;
    private final TaskDTOService taskDTOService;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(TaskRepository taskRepository, TaskDBService taskDBService, TaskDTOService taskDTOService, JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.taskRepository = taskRepository;
        this.taskDBService = taskDBService;
        this.taskDTOService = taskDTOService;
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    /**
     * Creates a new task based on the provided TaskRequestDTO.
     *
     * @param taskRequestDTO
     * @return ResponseSuccess
     */
    @Override
    @Transactional
    public ResponseSuccess create(TaskRequestDTO taskRequestDTO) {
        LOGGER.info("Request to save task {}", taskRequestDTO);
        try {
            EmployeeDepartmentDTO employeeDepartmentDTO = taskDBService.
                    getEmployeeIdAndDepartmentId(taskRequestDTO.employeeId());
            if (employeeDepartmentDTO == null) {
                LOGGER.error("Сотрудник с employeeId {} не найден", taskRequestDTO.employeeId());
                throw new DataNotFoundException("Сотрудник не найден");
            }
//            List<Long> getResourceIds = taskDBService.getResourceId(taskRequestDTO.resourceId());
            Task task = taskDTOService.toTaskEntity(employeeDepartmentDTO, taskRequestDTO);
            task = taskRepository.save(task);
            LOGGER.info("Successfully saved");

            return new ResponseSuccess("Успешно создано задание с id: " + task.getId());

        } catch (Exception e) {
            LOGGER.error("Error while saving task: {}", e.getMessage());
            throw new RuntimeException("Error while saving task", e);
        }
    }

    @Override
    public PageResponseDTO<TaskResponseDTO> getPageable(TaskFilter filter) {
        LOGGER.info("Запрос на получение всех задач");
        try {
            return taskDBService.getAllTasks(PageRequest.of(filter.getPage(), filter.getSize(), filter.getOrderedSortBy()));
        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при получении всех задач: {}", e.getMessage());
            throw new DataNotFoundException("Ошибка при получении всех задач: " + e.getMessage());
        }
    }

    /**
     * Changes the status of a task for a specific employee.
     *
     * @param id         the ID of the task to be updated
     * @param employeeId the ID of the employee associated with the task
     * @param status     the new status to be set for the task
     * @return a ResponseSuccess object indicating the success of the operation
     * @throws DataNotFoundException if the employee is not found in the task
     */

    @Override
    public ResponseSuccess updateStatus(Long id, String employeeId, String status) {
        try {
            LOGGER.info("Request to change task status by id: {}", id);

            String sql = """
                        UPDATE task_employee 
                        SET 
                            status = ?,
                            completed_date = CASE WHEN ? = 'COMPLETED' THEN NOW() ELSE NULL END
                        WHERE task_id = ? 
                          AND employee_id = ?
                    """;

            int updateRows = jdbcTemplate.update(sql,
                    status,     // status
                    status,     // для CASE WHEN
                    id,         // task_id
                    employeeId  // employee_id
            );

            if (updateRows == 0) {
                LOGGER.error("Сотрудник с employee_id {} не найден в задаче {}", employeeId, id);
                throw new DataNotFoundException("Сотрудник не найден в задаче");
            }

            LOGGER.info("Successfully changed task status by id: {}", id);
            return new ResponseSuccess("Успешно изменён статус задачи с id: " + id);

        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при изменении статуса задачи :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при изменении статуса задачи: " + id, e);
        }
    }


    @Override
    public ResponseSuccess delete(Long id) {
        try {
            LOGGER.info("Request to delete task by id: {}", id);
            Task task = taskRepository.findById(id).orElseThrow(() ->
                    new DataNotFoundException("Данные не найдены с id: " + id));
            taskRepository.delete(task);
            LOGGER.info("Successfully deleted task by id: {}", id);
            return new ResponseSuccess("Успешно удалено с id: " + id);
        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при удалении задачи :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при удалении задачи : " + id, e);
        }
    }

    @Override
    public void changeTaskEmployeeCompleteStatus(String employeeId, Long taskId) {
        try {
            LOGGER.info("Запрос на изменение статуса задачи для сотрудника");
            taskDBService.getOneTaskByTaskIdAndEmployeeId(employeeId, taskId);
            taskDBService.changeStatusAndDateTaskEmployee(employeeId, taskId);
            new ResponseSuccess("Успешно изменено статус задачи для сотрудника");
        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при изменении статуса задачи для сотрудника :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при изменении статуса задачи для сотрудника: " + e.getMessage());
        }

    }

    @Override
    @Transactional
    public ResponseSuccess update(Long id, TaskRequestDTO taskRequestDTO) {
        try {
            LOGGER.info("Запрос на обновление задачи по id: {}", id);
            Task task = taskRepository.findById(id).orElseThrow(() ->
                    new DataNotFoundException("Задача не найдена с id: " + id));
            task = taskDTOService.updateTaskFromDTO(task, taskRequestDTO);
            taskRepository.save(task);
            return new ResponseSuccess(
                    "Успешно обновлено задача с id: " + id
            );
        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при обновлении задачи :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при обновлении задачи: " + e.getMessage());
        }
    }

    @Override
    public TaskResponseDTO get(Long id, String userId) {
        try {
            return taskDBService.getTaskById(id, userId);
        } catch (
                DataNotFoundException e) {
            LOGGER.error("Ошибка при получении задачи :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при получении задачи: " + e.getMessage());
        }
    }

    @Override
    public PageResponseDTO<TaskResponseEmployeeDepartmentDTO> getTaskByEmployeeId(String employeeId, int page,
                                                                                  int size) {
        try {
            LOGGER.info("Запрос на получение задачи по id сотрудника: {}", employeeId);
            //            if (employeeResponses.isEmpty()) {
//                throw new DataNotFoundException("Задачи не найдены для сотрудника: " + fullName);
//            }
            return taskDBService.getByEmployeeId(employeeId, page, size);

        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при получении задачи по id сотрудника :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при получении задачи по id сотрудника: " + e.getMessage());
        }
    }

    @Override
    public TaskResponseDTO createTaskByDepartmentId(TaskRequestDTO taskRequestDTO, Long departmentId) {
        try {
            LOGGER.info("Запрос на сохранение задачи с department id: {}", departmentId);
            List<String> employeeIds = taskDBService.getEmployeeIdByDepartmentId(departmentId);
            if (employeeIds.isEmpty()) {
                throw new DataNotFoundException("Сотрудники не найдены");
            }
            EmployeeDepartmentDTO employeeDepartmentDTO = taskDBService.
                    getEmployeeIdAndDepartmentId(employeeIds);

            Task task = taskDTOService.toTaskEntity(employeeDepartmentDTO, taskRequestDTO);

            task = taskRepository.save(task);
            return null;
//            return taskDTOService.taskResponseDTO(task);
        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при сохранении задачи :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при сохранении задачи: " + e.getMessage());

        }

    }

    @Override
    public PageResponseDTO<TaskResponseDTO> getTaskByDepartmentId(String userId, int page,
                                                                  int size) {

        try {
            LOGGER.info("Запрос на получение задачи по id отдела");
            //            if (taskResponseDTOS.isEmpty()) {
//                throw new DataNotFoundException("Задачи не найдены для отдела: " + departmentId);
//            }
            return taskDBService.getAllTaskByDepartmentId(userId, page, size);

        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при получении задачи по id отдела :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при получении задачи по id отдела: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseSuccess updateTaskPriority(Long id, String priority) {
        try {
            LOGGER.info("Запрос на изменение приоритета задачи по id: {}", id);
            String sql = """
                    UPDATE task_employee e
                    SET priority = :priority
                    WHERE e.task_id = ? AND e.employee_id =:?
                    """;
            int updateRows = jdbcTemplate.update(sql, priority, id);
            if (updateRows == 0) {
                LOGGER.error("Сотрудник с employee_id {} не найден в задаче {}", id, id);
                throw new DataNotFoundException("Сотрудник не найден в задаче");
            }
            LOGGER.info("Успешно изменен приоритет задачи по id: {}", id);
            return new ResponseSuccess("Успешно изменен приоритет задачи с id: " + id);
        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при изменении приоритета задачи :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при изменении приоритета задачи: " + e.getMessage());
        }
    }

    @Override
    public TaskStatusResponseDTO getTasksByStatus(String status, String employeeId) {
        LOGGER.info("Запрос на получение задач по статусу: {}", status);
        int page = 0;
        int size = 50;

        try {
            return taskDBService.getTasksByStatus(employeeId, status, page, size);
        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при получении задач по статусу :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при получении задач по статусу: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponseDTO getNotificationByEmployeeId(String employeeId) {
        LOGGER.info("Запрос на получение уведомлений по id сотрудника: {}", employeeId);
        String sqlCount = """
                SELECT count(te.task_id)
                from task_employee te where te.employee_id = ?
                and te.notified = false
                """;

        String sqlUpdate = """
                UPDATE task_employee
                SET notified = true
                WHERE employee_id = ?
                AND notified = false
                """;

        try {
            Integer count = jdbcTemplate.queryForObject(sqlCount, Integer.class, employeeId);
            if (count == null) {
                throw new DataNotFoundException("Уведомления не найдены для сотрудника: " + employeeId);
            }
            jdbcTemplate.update(sqlUpdate, employeeId);

            return new NotificationResponseDTO(count);

        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при получении уведомлений по id сотрудника :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при получении уведомлений по id сотрудника: " + e.getMessage());
        }
    }

    @Override
    public ResponseSuccess updateOnlyUsersTask(String taskUUID, TaskUserRequestDTO taskUserRequestDTO) {
        LOGGER.info("Запрос на обновление задачи по id: {}", taskUUID);
        try {
            String updateSQL = """
                    UPDATE task_employee
                    SET
                        name = ?,
                        end_date = TO_DATE(?, 'DD.MM.YYYY'),
                        description = ?,
                        priority = ?
                    WHERE
                        taskuuid = ?::UUID
                    """;
            int updateRows = jdbcTemplate.update(updateSQL,
                    taskUserRequestDTO.name(),
                    taskUserRequestDTO.endDate(),
                    taskUserRequestDTO.description(),
                    taskUserRequestDTO.priority(),
                    taskUUID);

            if (updateRows == 0) {
                LOGGER.error("Задача с id {} не найдена", taskUUID);
                throw new DataNotFoundException("Задача не найдена");
            }
            LOGGER.info("Успешно обновлено");
            return new ResponseSuccess("Успешно обновлено задача с id: " + taskUUID);
        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при обновлении задачи :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при обновлении задачи: " + e.getMessage());
        }

    }

    @Override
    public ResponseSuccess deleteOnlyUserTask(String taskUUID) {
        LOGGER.info("Запрос на удаление задачи по id: {}", taskUUID);
        try {
            String sql = """
                    DELETE FROM  task_employee where taskuuid = ?::UUID
                    """;
            int updateRows = jdbcTemplate.update(sql, taskUUID);
            if (updateRows == 0) {
                LOGGER.error("Задача с id {} не найдена", taskUUID);
                throw new DataNotFoundException("Задача не найдена");
            }
            LOGGER.info("Успешно удалено");
            return new ResponseSuccess("Успешно удалено задача с id: " + taskUUID);
        } catch (DataNotFoundException e) {
            LOGGER.error("Ошибка при удалении задачи :{}", e.getMessage());
            throw new DataNotFoundException("Ошибка при удалении задачи: " + e.getMessage());
        }
    }

}