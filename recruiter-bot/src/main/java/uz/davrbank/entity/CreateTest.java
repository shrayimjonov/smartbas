package uz.davrbank.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "create_test")
public class CreateTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uz", column = @Column(name = "qtitle_uz", columnDefinition = "TEXT")),
            @AttributeOverride(name = "ru", column = @Column(name = "qtitle_ru", columnDefinition = "TEXT")),
    })
    private QuestionText testQuestion;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uz", column = @Column(name = "A_uz", columnDefinition = "TEXT")),
            @AttributeOverride(name = "ru", column = @Column(name = "A_ru", columnDefinition = "TEXT")),
    })
    private QuestionText questionA;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uz", column = @Column(name = "B_uz", columnDefinition = "TEXT")),
            @AttributeOverride(name = "ru", column = @Column(name = "B_ru", columnDefinition = "TEXT")),
    })
    private QuestionText questionB;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uz", column = @Column(name = "C_uz", columnDefinition = "TEXT")),
            @AttributeOverride(name = "ru", column = @Column(name = "C_ru", columnDefinition = "TEXT")),
    })
    private QuestionText questionC;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uz", column = @Column(name = "D_uz", columnDefinition = "TEXT")),
            @AttributeOverride(name = "ru", column = @Column(name = "D_ru", columnDefinition = "TEXT")),
    })
    private QuestionText questionD;
    private String correctAnswer;
    @Lob
    @Column(columnDefinition = "TEXT")
    private byte[] photo;


}
