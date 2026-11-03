package uz.davrbank.userinfobase.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "davrhub_files",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"davrhub_id", "doc_id"})
        }
)
public class DavrhubFile extends BaseEntity {
    private Long transId;
    private Long davrhubId;

    private String fileName;

    private String dtime;

    private String docId;

    private String state;

    private Integer year;

    private Integer quar;

    private Integer placeSrc;

    @Column(columnDefinition = "TEXT")
    private String commentText;

    private String nameDoc;

    private String nameState;

    private String minioObjectName;

    private Long fileSize;
}