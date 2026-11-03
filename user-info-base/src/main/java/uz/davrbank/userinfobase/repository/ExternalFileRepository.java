package uz.davrbank.userinfobase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.userinfobase.entity.ExternalFile;
import uz.davrbank.userinfobase.enums.SystemSource;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ExternalFileRepository extends JpaRepository<ExternalFile, Long> {

    interface ExternalFileKeyView {
        SystemSource getSource();

        String getExternalFileId();
    }

    List<ExternalFile> findAllByLoanId(String loanId);

    List<ExternalFile> findAllByLoanIdAndBranchId(String loanId, String branchId);

    @Query("select f.externalFileId from ExternalFile f where f.loanId = :loanId and f.source = :source")
    List<String> findExternalFileIdsByLoanIdAndSource(
            @Param("loanId") String loanId,
            @Param("source") SystemSource source
    );

    @Query("select f.externalFileId from ExternalFile f where f.source = :source and f.externalFileId in :externalFileIds")
    List<String> findExternalFileIdsBySourceAndExternalFileIdIn(
            @Param("source") SystemSource source,
            @Param("externalFileIds") Collection<String> externalFileIds
    );

    @Query("select f.source as source, f.externalFileId as externalFileId from ExternalFile f " +
            "where f.source in :sources and f.externalFileId in :externalFileIds")
    List<ExternalFileKeyView> findKeysBySourceInAndExternalFileIdIn(
            @Param("sources") Set<SystemSource> sources,
            @Param("externalFileIds") Collection<String> externalFileIds
    );
}
