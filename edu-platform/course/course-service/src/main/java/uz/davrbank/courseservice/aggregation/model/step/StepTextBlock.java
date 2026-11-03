package uz.davrbank.courseservice.aggregation.model.step;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StepTextBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    private Step step;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body; // основной текст

    private Integer orderInStep; // порядок отображения, если несколько блоков
}
