package uz.davrbank.assignmentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.assignmentservice.aggregation.dto.general.PageResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.general.ResponseSuccess;
import uz.davrbank.assignmentservice.aggregation.dto.task.TaskRequestDTO;
import uz.davrbank.assignmentservice.aggregation.dto.task.TaskResponseDTO;
import uz.davrbank.assignmentservice.aggregation.dto.task.TaskUserRequestDTO;
import uz.davrbank.assignmentservice.aggregation.filter.TaskFilter;
import uz.davrbank.assignmentservice.service.dao.TaskService;
import uz.davrbank.assignmentservice.utils.ApiConstants;

@Tag(name = "Задачи", description = "API для работы с задачами")
@RestController
@RequestMapping(ApiConstants.taskRootApi)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Создание задачи")
    @PostMapping
    public ResponseSuccess create(@RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        return taskService.create(taskRequestDTO);
    }

    @Operation(summary = "Получение всех задач с пагинация")
    @GetMapping
    public ResponseEntity<?> getPageable(@ParameterObject TaskFilter filter) {
        return ResponseEntity.ok(taskService.getPageable(filter));
    }

    @Operation(summary = "Изменение статуса задачи")
    @PostMapping(ApiConstants.status)
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestParam("employeeId") String employeeId,
                                          @RequestParam String status) {
        return ResponseEntity.ok(taskService.updateStatus(id, employeeId, status));
    }

    @Operation(summary = "Изменение приоритета задачи")
    @PostMapping("/priority/{id}")
    public ResponseEntity<?> updatePriority(@PathVariable Long id,
                                            @RequestParam String priority) {
        return ResponseEntity.ok(taskService.updateTaskPriority(id, priority));
    }

    @Operation(summary = "Удаление задачи")
    @DeleteMapping(ApiConstants.id)
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.delete(id));
    }

    @Operation(summary = "Изменение задачи")
    @PutMapping(ApiConstants.id)
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.ok(taskService.update(id, taskRequestDTO));
    }

    @Operation(summary = "Получение задачи по ID")
    @GetMapping(ApiConstants.id)
    public ResponseEntity<?> get(@PathVariable Long id,
                                 @RequestParam String userId) {
        return ResponseEntity.ok(taskService.get(id, userId));
    }

    @Operation(summary = "Получение задач по ID сотрудника")
    @GetMapping("/get/by/employee")
    public ResponseEntity<?> getEmployeeTasks(@RequestParam("employeeId") String id,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(taskService.getTaskByEmployeeId(id, page, size));
    }

    @Operation(summary = "Создание задач по ID отдела")
    @PostMapping("/create/by/department/{departmentId}")
    public ResponseEntity<?> createTaskByDepartmentId(@RequestBody @Valid TaskRequestDTO taskRequestDTO,
                                                      @PathVariable Long departmentId) {
        return ResponseEntity.ok(taskService.createTaskByDepartmentId(taskRequestDTO, departmentId));
    }

    @Operation(summary = "Получение задач по ID отдела")
    @GetMapping("/get/department/{id}")
    public ResponseEntity<?> getDepartmentTasks(@PathVariable String id,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "100") int size) {
        PageResponseDTO<TaskResponseDTO> pageResponseDTO =
                taskService.getTaskByDepartmentId(id, page, size);
        return ResponseEntity.ok(pageResponseDTO);
    }

    @Operation(summary = "Получение задач по статусу")
    @GetMapping("/get/by/status")
    public ResponseEntity<?> getByStatus(
            @RequestParam String status,
            @RequestParam String employeeId) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status, employeeId));
    }

    @Operation(summary = "Уведомление по ID сотрудника")
    @GetMapping("/get/notification")
    public ResponseEntity<?> getNotification(@RequestParam String employeeId) {
        return ResponseEntity.ok(taskService.getNotificationByEmployeeId(employeeId));
    }

    @Operation(summary = "Обновление задачи сотрудника")
    @PutMapping("/update/employee/task/{id}")
    public ResponseEntity<?> updateEmployeeTask(@PathVariable String id,
                                                @RequestBody TaskUserRequestDTO taskUserRequestDTO) {
        return ResponseEntity.ok(taskService.updateOnlyUsersTask(id, taskUserRequestDTO));
    }

    @DeleteMapping("/delete/employee/task/{id}")
    public ResponseEntity<?> deleteEmployeeTask(@PathVariable String id) {
        return ResponseEntity.ok(taskService.deleteOnlyUserTask(id));
    }
}
