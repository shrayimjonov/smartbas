package uz.davrbank.usermanualservice.aggregate.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import uz.davrbank.usermanualservice.aggregate.domain.base.BaseEntity;
import uz.davrbank.usermanualservice.aggregate.enums.PlatformType;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_manual_section",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_manual_section_name", columnNames = {"name"})
        }
)
public class UserManualSection extends BaseEntity {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "parent_id")
    Long parentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform_type", nullable = false)
    PlatformType platformType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    UserManualSection parent;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "parent", orphanRemoval = true)
    List<UserManualSection> children = new ArrayList<>();
}