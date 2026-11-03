package uz.davrbank.userinfobase.V2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.davrbank.userinfobase.V2.entity.Attachment;

import java.util.Optional;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    Optional<Attachment> findFirstByObjectName(String objectName);
}
