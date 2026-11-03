package uz.davrbank.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "message")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
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
    private Integer messageId;
    private LocalDateTime createAt;
    private LocalDateTime sendPostDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "photo_id")
    private PhotoUpload photoUpload;

}
