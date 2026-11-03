package uz.davrbank.userinfobase.entity.files;

import jakarta.persistence.*;
import lombok.*;
import uz.davrbank.userinfobase.entity.nci.LoanInfoEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "file_uploads")
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String minioObjectName;
    @Lob
    private byte[] file;
    private Long fileSize;
    private LocalDateTime uploadedAt;
    private String fileType;
    private String source;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "loan_info_id")
    private LoanInfoEntity loanInfo;

    @PrePersist
    @PreUpdate
    public void calculateFileSize() {
        if (this.file != null) {
            this.fileSize = (long) this.file.length;
        }
    }

}
