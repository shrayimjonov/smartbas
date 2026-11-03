package uz.davrbank.usermanualservice.aggregate.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.davrbank.usermanualservice.aggregate.domain.base.BaseEntity;
import uz.davrbank.usermanualservice.aggregate.enums.UserManualVersionStatus;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "user_manual_version",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_umv_version_manual", columnNames = {"version_number", "section_id"})
        }
)
public class UserManualVersion extends BaseEntity {

    @Column(name = "version_number", nullable = false, length = 20)
    String versionNumber;

    @Column(name = "section_id")
    Long sectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    UserManualSection section;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    UserManualVersionStatus status = UserManualVersionStatus.DRAFT;
}
