package uz.davrbank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_answers")
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Assuming there's a User entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_form_id", nullable = false)
    private CreateTest createTest; // The question the user is answering

    @Column(name = "selected_answer", nullable = false)
    private String selectedAnswer; // The option selected by the user
    @Column(name = "points", nullable = false)
    private Integer points;
    private LocalDateTime createdAt;
}