package uz.davrbank.educontrolservice.service.dao.task;

import uz.davrbank.educontrolservice.aggregate.dto.assigment.task.TaskRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.task.TaskUserRequestDTO;

public interface TaskInterface {

    Object createTask(TaskRequestDTO taskRequestDTO);

    Object getAllTasks(int page, int size, String sort);

    Object changeTaskStatus(Long id, String status);

    Object deleteTask(Long id);

    Object changeTaskEmployeeCompleteStatus(Long taskId, Boolean status);

    Object updateTask(Long id, TaskRequestDTO taskRequestDTO);

    Object getOneTask(Long id);

    Object getTaskByEmployeeId(int page, int size);

    Object creatTaskByDepartmentId(Long departmentId, TaskRequestDTO taskRequestDTO);

    Object getTaskByDepartmentId(int page, int size);

    Object changeTaskPriority(Long id, String priority);

    Object getTasksByStatus(String status);

    Object getNotificationByEmployeeId();

    Object updateOnlyUserTask(String taskId, TaskUserRequestDTO taskUserRequestDTO);

    Object deleteOnlyUserTask(String taskUUID);
}
