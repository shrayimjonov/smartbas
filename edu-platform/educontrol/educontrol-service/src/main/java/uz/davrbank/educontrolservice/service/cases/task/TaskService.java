package uz.davrbank.educontrolservice.service.cases.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.task.TaskRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.task.TaskUserRequestDTO;
import uz.davrbank.educontrolservice.feign.TaskClientFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.task.TaskInterface;

@Service
@Slf4j
public class TaskService implements TaskInterface {
    private final TaskClientFeign taskClientFeign;
    private final JwtTokenService jwtTokenService;

    public TaskService(TaskClientFeign taskClientFeign, JwtTokenService jwtTokenService, JwtTokenService jwtTokenService1) {
        this.taskClientFeign = taskClientFeign;
        this.jwtTokenService = jwtTokenService1;
    }


    @Override
    public Object createTask(TaskRequestDTO taskRequestDTO) {
        log.info("REQUEST (createTask) FROM [assigment-service]");
        return taskClientFeign.createTask(taskRequestDTO);
    }

    @Override
    public Object getAllTasks(int page, int size, String sort) {
        log.info("REQUEST (getAllTasks) FROM [assigment-service]");
        return taskClientFeign.getAllTasks(page, size, sort);
    }

    @Override
    public Object changeTaskStatus(Long id, String status) {
        log.info("REQUEST (changeTaskStatus) FROM [assigment-service]");
        String employeeId = jwtTokenService.getCurrentUserId();
        return taskClientFeign.changeTaskStatus(id, employeeId, status);
    }

    @Override
    public Object deleteTask(Long id) {
        log.info("REQUEST (deleteTask) FROM [assigment-service]");
        return taskClientFeign.deleteTask(id);
    }

    @Override
    public Object changeTaskEmployeeCompleteStatus(Long taskId, Boolean status) {
        log.info("REQUEST (changeTaskEmployeeCompleteStatus) FROM [assigment-service]");
        String employeeId = jwtTokenService.getCurrentUserId();
        return taskClientFeign.changeEmployeeTaskStatus(employeeId, taskId, status);
    }

    @Override
    public Object updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        log.info("REQUEST (updateTask) FROM [assigment-service] ");
        return taskClientFeign.updateTask(id, taskRequestDTO);
    }

    @Override
    public Object getOneTask(Long id) {
        log.info("REQUEST (getOneTask) FROM [assigment-service]");
        String employeeId = jwtTokenService.getCurrentUserId();
        return taskClientFeign.getOneTaskData(id, employeeId);
    }

    @Override
    public Object getTaskByEmployeeId(int page, int size) {
        log.info("REQUEST (getTaskByEmployeeId) FROM [assigment-service]");
        String employeeId = jwtTokenService.getCurrentUserId();
        log.info(employeeId);
        return taskClientFeign.getEmployeeTasks(employeeId, page, size);
    }

    @Override
    public Object creatTaskByDepartmentId(Long departmentId, TaskRequestDTO taskRequestDTO) {
        log.info("REQUEST (creatTaskByDepartmentId) FROM [assigment-service]");
        return taskClientFeign.createDepartmentTask(departmentId, taskRequestDTO);
    }

    @Override
    public Object getTaskByDepartmentId( int page, int size) {
        log.info("REQUEST (getTaskByDepartmentId) FROM [assigment-service]");
        String employeeId = jwtTokenService.getCurrentUserId();
        return taskClientFeign.getDepartmentTasks(employeeId, page, size);
    }

    @Override
    public Object changeTaskPriority(Long id, String priority) {
        log.info("REQUEST (changeTaskPriority) FROM [assigment-service]");
        String employeeId = jwtTokenService.getCurrentUserId();

        return taskClientFeign.changeTaskPriority(id, employeeId, priority);
    }

    @Override
    public Object getTasksByStatus(String status) {
        log.info("REQUEST (getTasksByStatus) FROM [assigment-service]");
        String employeeId = jwtTokenService.getCurrentUserId();

        return taskClientFeign.getTasksByStatus(status, employeeId);
    }

    @Override
    public Object getNotificationByEmployeeId() {
        log.info("REQUEST (getNotificationByEmployeeId) FROM [assigment-service]");
        String employeeId = jwtTokenService.getCurrentUserId();
        return taskClientFeign.getNotificationByEmployeeId(employeeId);
    }

    @Override
    public Object updateOnlyUserTask(String taskId,
                                     TaskUserRequestDTO taskUserRequestDTO) {
        log.info("REQUEST (updateOnlyUserTask) FROM [assigment-service]");
//        String userId = jwtTokenService.getCurrentUserId();
        return taskClientFeign.updateOnlyUserTask(taskId, taskUserRequestDTO);
    }

    @Override
    public Object deleteOnlyUserTask(String taskUUID) {
        log.info("REQUEST (deleteOnlyUserTask) FROM [assigment-service]");
        return taskClientFeign.deleteOnlyUserTask(taskUUID);
    }

}
