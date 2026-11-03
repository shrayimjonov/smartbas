package uz.davrbank.partnervalidationservice.logging.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.davrbank.partnervalidationservice.logging.entity.PartnerValidationLog;

public interface PartnerValidationLogRepository
        extends JpaRepository<PartnerValidationLog, UUID>, JpaSpecificationExecutor<PartnerValidationLog> {
}
