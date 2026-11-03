package uz.davrbank.assignmentservice.service.cases.task;

import org.springframework.stereotype.Component;
import uz.davrbank.assignmentservice.aggregation.dto.task.EmployeeDepartmentDTO;
import uz.davrbank.assignmentservice.aggregation.dto.task.TaskRequestDTO;
import uz.davrbank.assignmentservice.aggregation.dto.task.TaskResponseDTO;
import uz.davrbank.assignmentservice.aggregation.exception.condition.DataNotFoundException;
import uz.davrbank.assignmentservice.aggregation.model.task.EmployeeTask;
import uz.davrbank.assignmentservice.aggregation.model.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TaskDTOService {
    private final TaskDBService taskDBService;

    public TaskDTOService(TaskDBService taskDBService) {
        this.taskDBService = taskDBService;
    }


//    public TaskResponseDTO taskResponseDTO(Task task) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//
//        return new TaskResponseDTO(
//                task.getId(),
//                task.getName(),
//                task.getStartDate().format(formatter),
//                task.getEndDate().format(formatter),
//                task.getDescription(),
//                task.getScore(),
//        );
//    }


    public Task toTaskEntity(EmployeeDepartmentDTO employeeDepartmentDTO,
                             TaskRequestDTO taskRequestDTO) {

        Task task = new Task();
        task.setName(taskRequestDTO.name());
        task.setEmployeeId(toEmployeeTask(employeeDepartmentDTO, taskRequestDTO));
        task.setDescription(taskRequestDTO.description());
        task.setScore(taskRequestDTO.score());
        task.setStartDate(parseFlexibleDate(taskRequestDTO.startDate()));
        task.setEndDate(parseFlexibleDate(taskRequestDTO.endDate()));
        return task;
    }

    private LocalDate parseFlexibleDate(String dateStr) {
        System.out.println("parseFlexibleDate input: [" + dateStr + "]");
        DateTimeFormatter dotFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter dashFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            return LocalDate.parse(dateStr.trim(), dotFormatter);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(dateStr.trim(), dashFormatter);
        }
    }



    // Метод преобразует списки fullName и departmentId в список EmployeeTask
    public List<EmployeeTask> toEmployeeTask(EmployeeDepartmentDTO employeeDepartmentDTO,
                                             TaskRequestDTO taskRequestDTO) {

        if (employeeDepartmentDTO.employeeId() == null || employeeDepartmentDTO.departmentId() == null ||
                employeeDepartmentDTO.employeeId().isEmpty() || employeeDepartmentDTO.departmentId().isEmpty()) {
            return Collections.emptyList();
        }

        if (employeeDepartmentDTO.employeeId().size() != employeeDepartmentDTO.departmentId().size()) {
            throw new IllegalArgumentException("Размер списков employeeIds и departmentIds должен совпадать");
        }

        return IntStream.range(0, employeeDepartmentDTO.employeeId().size())
                .mapToObj(i -> new EmployeeTask(
                        UUID.randomUUID(),
                        employeeDepartmentDTO.employeeId().get(i),
                        employeeDepartmentDTO.departmentId().get(i),
                        taskRequestDTO.name(),
                        parseFlexibleDate(taskRequestDTO.startDate()),
                        parseFlexibleDate(taskRequestDTO.endDate()),
                        taskRequestDTO.description(),
                        null,
                        EmployeeTask.Status.CREATED,
                        EmployeeTask.Priority.LOW,
                        false))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public Task updateTaskFromDTO(Task task, TaskRequestDTO taskRequestDTO) {

        if (taskRequestDTO.startDate() != null) {
            task.setStartDate(parseFlexibleDate(taskRequestDTO.startDate()));
        }
        if (taskRequestDTO.endDate() != null) {
            task.setEndDate(parseFlexibleDate(taskRequestDTO.endDate()));
        }
        if (taskRequestDTO.name() != null) {
            task.setName(taskRequestDTO.name());
        }

        if (taskRequestDTO.employeeId() != null) {
            EmployeeDepartmentDTO employeeDepartmentDTO =
                    taskDBService.getEmployeeIdAndDepartmentId(taskRequestDTO.employeeId());

            if (employeeDepartmentDTO != null) {
                task.setEmployeeId(toEmployeeTask(employeeDepartmentDTO, taskRequestDTO));
            } else {
                task.setEmployeeId(new ArrayList<>()); // Устанавливаем пустой список вместо null
            }
        }

        return task;

    }
}
