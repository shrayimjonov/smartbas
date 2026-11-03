package uz.davrbank.assignmentservice.service.dao;

import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.assignmentservice.aggregation.dto.task.*;
import uz.davrbank.assignmentservice.aggregation.filter.TaskFilter;

public interface TaskService {

    //Админ
    ResponseSuccess create(TaskRequestDTO taskRequestDTO);

    //Админ, Глава Отдела
    PageResponseDTO<TaskResponseDTO> getPageable(TaskFilter filter);

    //Сотрудник
    ResponseSuccess updateStatus(Long id, String employeeId, String status);

    //Админ, Глава Отдела
    ResponseSuccess delete(Long id);

    void changeTaskEmployeeCompleteStatus(String employeeId, Long taskId);

    //Админ, Глава Отдела
    ResponseSuccess update(Long id, TaskRequestDTO taskRequestDTO);

    //Админ, Сотрудник
    TaskResponseDTO get(Long id, String userId);

    PageResponseDTO<TaskResponseEmployeeDepartmentDTO> getTaskByEmployeeId(String employeeId, int page, int size);

    TaskResponseDTO createTaskByDepartmentId(TaskRequestDTO taskRequestDTO, Long departmentId);

    PageResponseDTO<TaskResponseDTO> getTaskByDepartmentId(String userId, int page, int size);

    ResponseSuccess updateTaskPriority(Long id, String priority);

    TaskStatusResponseDTO getTasksByStatus(String status, String employeeId);

    NotificationResponseDTO getNotificationByEmployeeId(String employeeId);

    ResponseSuccess updateOnlyUsersTask(String taskUUID,
                                        TaskUserRequestDTO taskUserRequestDTO);

    ResponseSuccess deleteOnlyUserTask(String taskUUID);


}
