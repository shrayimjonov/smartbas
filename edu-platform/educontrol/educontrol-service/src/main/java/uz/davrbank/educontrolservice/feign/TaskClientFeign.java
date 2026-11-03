package uz.davrbank.educontrolservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.task.TaskRequestDTO;
import uz.davrbank.educontrolservice.aggregate.dto.assigment.task.TaskUserRequestDTO;
import uz.davrbank.educontrolservice.feign.config.AssignmentFeignServiceConfiguration;

@FeignClient(name = "assignment-service", contextId = "taskClient", configuration = AssignmentFeignServiceConfiguration.class)
public interface TaskClientFeign {

    @PostMapping("/api/v1/task/create")
    Object createTask(@RequestBody TaskRequestDTO taskRequestDTO);

    @GetMapping("/api/v1/task/get/all")
    Object getAllTasks(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "100") int size,
                       @RequestParam(defaultValue = "id,desc") String sort);


    @PostMapping("/api/v1/task/change/status")
    Object changeTaskStatus(@RequestParam("taskId") Long id,
                            @RequestParam("employeeId") String employeeId,
                            @RequestParam String status);

    @DeleteMapping("/api/v1/task/delete/{id}")
    Object deleteTask(@PathVariable Long id);

    @PostMapping("/api/v1/task/change/employee/status")
    Object changeEmployeeTaskStatus(@RequestParam("employeeId") String employeeId,
                                    @RequestParam Long taskId,
                                    @RequestParam Boolean status);

    @PutMapping("/api/v1/task/update/{id}")
    Object updateTask(@PathVariable Long id,
                      @RequestBody TaskRequestDTO taskRequestDTO);

    @GetMapping("/api/v1/task/get/one")
    Object getOneTaskData(@RequestParam Long id,
                          @RequestParam String userId);

    @GetMapping("/api/v1/task/get/by/employee")
    Object getEmployeeTasks(@RequestParam("employeeId") String id,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "100") int size);

    @PostMapping("/api/v1/task/create/by/department/{departmentId}")
    Object createDepartmentTask(@PathVariable Long departmentId,
                                @RequestBody TaskRequestDTO taskRequestDTO);

    @GetMapping("/api/v1/task/get/department/{id}")
    Object getDepartmentTasks(@PathVariable String id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "100") int size);

    @PostMapping("/api/v1/task/change/priority")
    Object changeTaskPriority(@RequestParam("taskId") Long id,
                              @RequestParam("employeeId") String employeeId,
                              @RequestParam String priority);


    @GetMapping("/api/v1/task/get/by/status")
    Object getTasksByStatus(@RequestParam String status,
                            @RequestParam String employeeId);

    @GetMapping("/api/v1/task/get/notification")
    Object getNotificationByEmployeeId(@RequestParam String employeeId);

    @PutMapping("/api/v1/task/update/employee/task/{id}")
    Object updateOnlyUserTask(@PathVariable String id,
                              TaskUserRequestDTO taskUserRequestDTO);

    @DeleteMapping("/api/v1/task/delete/employee/task/{id}")
    Object deleteOnlyUserTask(@PathVariable String id);
}
