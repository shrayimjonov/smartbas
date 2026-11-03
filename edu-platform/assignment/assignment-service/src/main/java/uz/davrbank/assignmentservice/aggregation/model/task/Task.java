package uz.davrbank.assignmentservice.aggregation.model.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task", indexes = {
        @Index(name = "idx_task_id", columnList = "id")
})
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private Double score;

    @ElementCollection
    @CollectionTable(name = "task_employee", joinColumns = @JoinColumn(name = "task_id"))
    private List<EmployeeTask> employeeId = new ArrayList<>();

//    @ElementCollection
//    @CollectionTable(name = "task_resource", joinColumns = @JoinColumn(name = "task_id"))
//    @Column(name = "resource_id")
//    private List<Long> resourceId = new ArrayList<>();





}

