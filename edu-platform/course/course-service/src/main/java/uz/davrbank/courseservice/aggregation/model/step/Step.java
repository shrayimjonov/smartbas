package uz.davrbank.courseservice.aggregation.model.step;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.davrbank.courseservice.aggregation.model.course.Course;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "step")
public class Step implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Step(String name) {
        this.name = name;
    }

    public Step(Long id) {
        this.id = id;
    }
}
