package uz.davrbank.userinfobase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.userinfobase.entity.DavrhubFile;

import java.util.Collection;
import java.util.List;


@Repository
public interface DavrhubFileRepository extends JpaRepository<DavrhubFile, Long> {
    List<DavrhubFile> findByTransId(Long transId);

    long countByTransId(Long transId);

    @Query("select f.docId from DavrhubFile f where f.transId = :transId")
    List<String> findDocIdsByTransId(@Param("transId") Long transId);

    @Query("select f.docId from DavrhubFile f where f.transId = :transId and f.docId in :docIds")
    List<String> findDocIdsByTransIdAndDocIdIn(
            @Param("transId") Long transId,
            @Param("docIds") Collection<String> docIds
    );

}
