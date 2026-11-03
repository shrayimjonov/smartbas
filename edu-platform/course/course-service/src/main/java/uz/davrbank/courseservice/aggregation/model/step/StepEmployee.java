package uz.davrbank.courseservice.aggregation.model.step;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.davrbank.courseservice.aggregation.enums.Status;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "step_employee")
public class StepEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_id")
    private Long stepId;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "score")
    private Double score = 0.0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedDate = LocalDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NOT_STARTED;

    public StepEmployee(Long stepId, String employeeId, Boolean isCompleted, Status status) {
        this.stepId = stepId;
        this.employeeId = employeeId;
        this.isCompleted = isCompleted;
        this.status = status;
    }
}
