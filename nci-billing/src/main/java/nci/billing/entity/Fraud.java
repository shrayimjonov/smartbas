package nci.billing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "fraud",
        indexes = {
                @Index(name = "loan", columnList = "loanId"),
                @Index(name = "paymentId", columnList = "paymentId"),
                @Index(name = "fraud_created_date", columnList = "created_date")
        })
@NoArgsConstructor
public class Fraud extends AbstractEntity {
    @Id
    @SequenceGenerator(
            name = "fraud_id_seq",
            sequenceName = "fraud_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fraud_id_seq"
    )
    private Long id;
    private Boolean isFraudster;
    private String loanId;
    private String paymentId;
    private Long amount = 0L;

    private String systemType;
    private String reason;

    public Fraud(Boolean isFraudster, String loanId, String paymentId, Long amount, String systemType, String reason) {
        this.isFraudster = isFraudster;
        this.loanId = loanId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.systemType = systemType;
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Fraud fraud = (Fraud) o;
        return id != null && Objects.equals(id, fraud.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
