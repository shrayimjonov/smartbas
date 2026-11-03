package uz.davrbank.usermanualservice.aggregate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.usermanualservice.aggregate.domain.UserManualVersion;
import uz.davrbank.usermanualservice.aggregate.enums.UserManualVersionStatus;

import java.util.List;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@Repository
public interface UserManualVersionRepository extends JpaRepository<UserManualVersion, Long> {
    boolean existsByVersionNumberIgnoreCaseAndSectionId(String versionNumber, Long sectionId);

    boolean existsByVersionNumberIgnoreCaseAndSectionIdAndIdNot(String versionNumber, Long sectionId, Long id);

    boolean existsBySectionId(Long sectionId);

    List<UserManualVersion> findAllByStatusAndSectionIdOrderByIdAsc(UserManualVersionStatus status, Long sectionId);
}
