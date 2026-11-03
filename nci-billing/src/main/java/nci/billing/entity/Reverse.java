package nci.billing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nci.billing.model.enums.PayStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@ToString
@Entity(name = "reverse")
@Table(indexes = {
        @Index(name = "tran_created_date", columnList = "created_date")
})
public class Reverse extends AbstractEntity {
    @SequenceGenerator(name = "rever_seq",
            sequenceName = "rever_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "rever_seq",
            strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> request;
    @Column(columnDefinition = "TEXT")
    private String response;
    @Enumerated(EnumType.STRING)
    private PayStatus status;


}
