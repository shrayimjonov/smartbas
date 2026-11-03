package uz.davrbank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileUpload file;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department; // Add this field
    private String language;   // New field for Language

    private LocalDateTime createdAt;
    @Column(columnDefinition = "TEXT")
    private String notes;
    @OneToMany
    private List<TestResult> testResultList = new ArrayList<>();

    @Column(unique = true)
    private String username;

    private String password;

    private String roles;

}

