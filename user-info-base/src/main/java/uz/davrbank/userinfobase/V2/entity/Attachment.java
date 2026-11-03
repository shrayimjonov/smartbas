package uz.davrbank.userinfobase.V2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(
        name = "attachment",
        indexes = {
                @Index(name = "idx_attachment_object_name", columnList = "object_name")
        }
)
public class Attachment {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, unique = true)
    UUID id;

    @Column(name = "bucket_name")
    String bucketName;

    @Column(name = "object_name", columnDefinition = "text", unique = true)
    String objectName;//this is the unique identifier

    @Column(name = "original_name", nullable = false, columnDefinition = "text")
    String originalName;

    @Column(name = "display_name", nullable = false)
    String displayName;

    @Column(name = "attachment_url", nullable = false)
    String attachmentUrl;

    @Column(name = "source", nullable = false)
    String source;

    @Column(name = "file_size")
    Long fileSize;

    @Column(name = "content_type")
    String contentType;
}
