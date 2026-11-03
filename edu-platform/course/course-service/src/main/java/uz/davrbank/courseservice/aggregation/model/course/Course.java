package uz.davrbank.courseservice.aggregation.model.course;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int timeComplete;
    private Long categoryId;
    private Long departmentId;
    private Double score = 0.0;
    private Boolean deleted = false;
    private Boolean isPublic = true;

    public Course(Long id) {
        this.id = id;
    }
}