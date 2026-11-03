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
        name = "loan_attachment",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"branch_id", "loan_ext_id", "attachment_id"})
        },
        indexes = {
                @Index(name = "idx_loan_attachment_branch_loan", columnList = "branch_id, loan_ext_id"),
                @Index(name = "idx_loan_attachment_branch_loan_attachment", columnList = "branch_id, loan_ext_id, attachment_id")
        }
)
public class LoanAttachment {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, unique = true)
    UUID id;


    @Column(name = "branch_id", nullable = false)
    String branchId;


    @Column(name = "loan_ext_id", nullable = false)
    String loanExtId;


    @Column(name = "attachment_id", nullable = false)
    UUID attachmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", updatable = false, insertable = false)
    Attachment attachment;
}
