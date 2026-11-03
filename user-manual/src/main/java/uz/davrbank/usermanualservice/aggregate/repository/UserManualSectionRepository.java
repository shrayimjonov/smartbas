package uz.davrbank.usermanualservice.aggregate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.usermanualservice.aggregate.domain.UserManualSection;
import uz.davrbank.usermanualservice.aggregate.enums.PlatformType;

import java.util.List;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@Repository
public interface UserManualSectionRepository extends JpaRepository<UserManualSection, Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name,Long id);

    List<UserManualSection> findAllByPlatformTypeOrderByIdAsc(PlatformType platformType);

    List<UserManualSection> findAllByParentIdOrderByIdAsc(Long parentId);

    boolean existsByParentId(Long parentId);
}
