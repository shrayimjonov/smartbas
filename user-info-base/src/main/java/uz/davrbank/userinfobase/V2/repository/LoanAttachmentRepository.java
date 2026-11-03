package uz.davrbank.userinfobase.V2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.davrbank.userinfobase.V2.entity.LoanAttachment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanAttachmentRepository extends JpaRepository<LoanAttachment, UUID> {

    interface LoanAttachmentFileView {
        UUID getAttachmentId();

        String getObjectName();

        String getOriginalName();

        String getDisplayName();

        String getAttachmentUrl();

        String getSource();

        Long getFileSize();
    }


    Optional<LoanAttachment> findFirstByLoanExtIdAndBranchIdAndAttachmentId(String loanExtId, String branchId, UUID attachmentId);

    List<LoanAttachment> findAllByLoanExtIdAndBranchId(String loanExtId, String branchId);

    @Query("""
            select la
            from LoanAttachment la
            join fetch la.attachment
            where la.loanExtId = :loanExtId
              and la.branchId = :branchId
            """)
    List<LoanAttachment> findAllWithAttachmentByLoanExtIdAndBranchId(
            @Param("loanExtId") String loanExtId,
            @Param("branchId") String branchId
    );

    @Query("""
            select a.id as attachmentId,
                   a.objectName as objectName,
                   a.originalName as originalName,
                   a.displayName as displayName,
                   a.attachmentUrl as attachmentUrl,
                   a.source as source,
                   a.fileSize as fileSize
            from LoanAttachment la
            join Attachment a on a.id = la.attachmentId
            where la.loanExtId = :loanExtId
              and la.branchId = :branchId
            order by a.source, a.originalName
            """)
    List<LoanAttachmentFileView> findFileViewsByLoanExtIdAndBranchId(
            @Param("loanExtId") String loanExtId,
            @Param("branchId") String branchId
    );
}
