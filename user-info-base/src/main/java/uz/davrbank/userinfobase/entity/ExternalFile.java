package uz.davrbank.userinfobase.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.davrbank.userinfobase.enums.SystemSource;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "external_files",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"source", "externalFileId"})
        },
        indexes = {
                @Index(name = "idx_loanId", columnList = "loanId"),
                @Index(name = "idx_source", columnList = "source")
        }
)
public class ExternalFile extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private SystemSource source;

    private String loanId;
    private String branchId;

    private String fileName;

    private String minioObjectName;

    private Long fileSize;

    private String mimeType;
    @Column(name = "name_doc")
    private String nameDoc;

    private String externalFileId;
}
