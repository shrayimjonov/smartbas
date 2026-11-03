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
@Table(name = "archive")
public class Archive {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uz", column = @Column(name = "title_uz", columnDefinition = "TEXT")),
            @AttributeOverride(name = "ru", column = @Column(name = "title_ru", columnDefinition = "TEXT")),
            @AttributeOverride(name = "en", column = @Column(name = "title_en", columnDefinition = "TEXT"))
    })
    private LocalizedText title;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "uz", column = @Column(name = "description_uz", columnDefinition = "TEXT")),
            @AttributeOverride(name = "ru", column = @Column(name = "description_ru", columnDefinition = "TEXT")),
            @AttributeOverride(name = "en", column = @Column(name = "description_en", columnDefinition = "TEXT"))
    })
    private LocalizedText description;

    private LocalDateTime deletedDate;
    private Integer messageId;
}
