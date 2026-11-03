package uz.davrbank.educontrolservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.task.TaskRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.task.TaskUserRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.task.TaskInterface;


//@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DAVR', 'ROLE_HR')")
@RestController
@RequestMapping("/api/v1/educontrol/task")
public class TaskController {

    private final TaskInterface taskInterface;

    public TaskController(TaskInterface taskInterface) {
        this.taskInterface = taskInterface;
    }


    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createTask(@RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Задача успешно создана",
                        taskInterface.createTask(taskRequestDTO)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT', 'ROLE_USER')")
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Object>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "100") int size,
                                                           @RequestParam(defaultValue = "id,desc") String sort) {
        return ResponseEntity.ok(ApiResponse.of("Список задач получен",
                taskInterface.getAllTasks(page, size, sort)));
    }


    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_USER')")
    @PostMapping("/change/status")
    public ResponseEntity<ApiResponse<Object>> changeTaskStatus(@RequestParam("taskId") Long id,
                                                                @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.of("Статус задачи успешно изменён",
                taskInterface.changeTaskStatus(id, status)));
    }

//    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
//        return ResponseEntity.ok(taskInterface.deleteTask(id));
//    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @PostMapping("/change/employee/status")
    public ResponseEntity<ApiResponse<Object>> changeEmployeeTaskCompleteStatus(@RequestParam Long taskId,
                                                                                @RequestParam Boolean status) {
        return ResponseEntity.ok(ApiResponse.of("Статус выполнения задачи сотрудником изменён",
                taskInterface.changeTaskEmployeeCompleteStatus(taskId, status)));
    }

//    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO taskRequestDTO) {
//        return ResponseEntity.ok(taskInterface.updateTask(id, taskRequestDTO));
//    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT', 'ROLE_USER')")
    @GetMapping("/get/one/{id}")
    public ResponseEntity<ApiResponse<Object>> getOneTaskData(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Информация о задаче получена",
                taskInterface.getOneTask(id)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT', 'ROLE_USER')")
    @GetMapping("/get/by/employee")
    public ResponseEntity<ApiResponse<Object>> getEmployeeTasks(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "100") int size) {
        return ResponseEntity.ok(ApiResponse.of("Список задач сотрудника получен",
                taskInterface.getTaskByEmployeeId(page, size)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @GetMapping("/get/department")
    public ResponseEntity<ApiResponse<Object>> getDepartmentTasks(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "100") int size) {
        return ResponseEntity.ok(ApiResponse.of("Список задач департамента получен",
                taskInterface.getTaskByDepartmentId(page, size)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
    @PostMapping("/create/by/department/{departmentId}")
    public ResponseEntity<ApiResponse<Object>> createTaskByDepartmentId(@PathVariable Long departmentId,
                                                                        @RequestBody TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Задача для департамента успешно создана",
                        taskInterface.creatTaskByDepartmentId(departmentId, taskRequestDTO)));
    }

//    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT')")
//    @PostMapping("/change/priority")
//    public ResponseEntity<?> changeTaskPriority(@RequestParam("taskId") Long id,
//                                                @RequestParam String priority) {
//        return ResponseEntity.ok(taskInterface.changeTaskPriority(id, priority));
//    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT', 'ROLE_USER')")
    @GetMapping("/get/by/status")
    public ResponseEntity<ApiResponse<Object>> getByStatus(@RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.of("Список задач по статусу получен",
                taskInterface.getTasksByStatus(status)));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT', 'ROLE_USER')")
    @GetMapping("/get/notification")
    public ResponseEntity<ApiResponse<Object>> getNotificationByEmployeeId() {
        return ResponseEntity.ok(ApiResponse.of("Уведомления получены",
                taskInterface.getNotificationByEmployeeId()));
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_DEPARTMENT', 'ROLE_USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Object>> updateUserTasks(@PathVariable String id,
                                                               @RequestBody TaskUserRequestDTO taskRequestDTO) {
        return ResponseEntity.ok(ApiResponse.of("Задача успешно обновлена",
                taskInterface.updateOnlyUserTask(id, taskRequestDTO)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteOnlyUserTask(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.of("Задача успешно удалена",
                taskInterface.deleteOnlyUserTask(id)));
    }
}

