package uz.davrbank.assignmentservice.aggregation.model.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmployeeTask {

    private UUID taskUUID;

    private String employeeId;

    private Long departmentId;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;


    public enum Status{
        CREATED,
        IN_PROGRESS,
        REJECTED,
        COMPLETED
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    private boolean notified;


}