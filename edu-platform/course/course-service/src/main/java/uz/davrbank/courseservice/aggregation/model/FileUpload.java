package uz.davrbank.courseservice.aggregation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.davrbank.courseservice.aggregation.enums.ResourceType;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "extension")
    private String extension;
    @Column(name = "path")
    private String path;
    @Column(name = "size")
    private String size;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadedAt;
    @Column(name = "resource_id", nullable = false)
    private Long resourceId = 0L;

    @Column(name = "resource_type")
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
    @Column(name = "description")
    private String description;

    @Column(name = "duration", nullable = false)
    private Double duration = 0.0;

    @Column(name = "length")
    private Long length = 0L; //length
}