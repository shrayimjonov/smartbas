package uz.davrbank.accounttocardservice.aggregate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.davrbank.accounttocardservice.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@ToString
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    @JsonIgnore
    private LocalDateTime createdTime = DateUtils.getDateTimeNow();

    @LastModifiedDate
    @Column(name = "updated_time")
    @JsonIgnore
    private LocalDateTime updatedTime = DateUtils.getDateTimeNow();

    @Column(name = "created_date")
    @JsonIgnore
    private LocalDate createdDate = DateUtils.getDateNow();

    @CreatedBy
    @JsonIgnore
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    @JsonIgnore
    private String lastModifiedBy;
}
