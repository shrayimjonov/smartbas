package nci.billing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@MappedSuperclass
public class AbstractEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @CreatedDate
    @Column(name = "created_time", updatable = false, nullable = false)
    private ZonedDateTime createdTime = ZonedDateTime.now();
    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDate createdDate = LocalDate.now(ZoneId.of("Asia/Tashkent"));
    @LastModifiedDate
    @Column(name = "updated_time")
    private ZonedDateTime updatedTime = ZonedDateTime.now();





}
