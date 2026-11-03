package uz.davrbank.usermanualservice.aggregate.domain.base;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@Getter
@Setter
@ToString(callSuper = true)
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "usermanual_seq", sequenceName = "usermanual_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usermanual_seq")
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "created_date", nullable = false)
    LocalDateTime createdDate;

    @Column(name = "last_modified_date", nullable = false)
    LocalDateTime lastModifiedDate;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.lastModifiedDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedDate = LocalDateTime.now();
    }
}