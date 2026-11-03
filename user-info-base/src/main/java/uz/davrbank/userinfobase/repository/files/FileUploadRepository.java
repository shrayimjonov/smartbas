package uz.davrbank.userinfobase.repository.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.userinfobase.entity.files.FileUpload;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    interface MirrorCandidateView {
        Long getId();

        String getFileName();

        String getMinioObjectName();

        Long getFileSize();

        String getFileType();

        String getSource();
    }

    List<FileUpload> findAllByOrderByUploadedAtDesc();

    List<FileUpload> findByLoanInfo_LoanExtIdOrderByUploadedAtDesc(String loanExtId);

    List<FileUpload> findByLoanInfo_LoanIdAndSourceOrderByUploadedAtDesc(String loanId, String source);

    boolean existsByFileNameAndFileSize(String fileName, Long fileSize);


    Optional<FileUpload> findFirstByLoanInfo_LoanExtIdAndFileNameAndFileSize(String loanExtId, String fileName, Long fileSize);

    Optional<FileUpload> findFirstByLoanInfo_LoanExtIdAndLoanInfo_BranchIdAndFileNameAndFileSize(String loanExtId, String branchId, String fileName, Long fileSize);


    List<FileUpload> findByLoanInfo_LoanExtIdAndSourceOrderByUploadedAtDesc(String loanExtId, String source);

    List<FileUpload> findBySourceOrderByUploadedAtDesc(String source);

    List<FileUpload> findAllBySourceAndLoanInfo_LoanExtIdOrderByUploadedAtDesc(String source, String loanExtId);

    @Query("select f.id as id, f.fileName as fileName, f.minioObjectName as minioObjectName, " +
            "f.fileSize as fileSize, f.fileType as fileType, f.source as source " +
            "from FileUpload f " +
            "where f.source in :sources and " +
            "((:loanId is not null and f.loanInfo.loanId = :loanId) " +
            "or (:loanExtId is not null and f.loanInfo.loanExtId = :loanExtId)) " +
            "order by f.uploadedAt desc")
    List<MirrorCandidateView> findMirrorCandidatesForExternalSync(
            @Param("loanId") String loanId,
            @Param("loanExtId") String loanExtId,
            @Param("sources") Collection<String> sources
    );

    List<MirrorCandidateView> findAllBySourceInAndLoanInfo_LoanExtIdAndLoanInfo_BranchId(Collection<String> source, String loanExtId, String branchId);

    List<FileUpload> findAllByLoanInfo_LoanExtIdAndLoanInfo_BranchIdAndSource(String loanExtId, String branchId, String source);
}
