package uz.davrbank.payment.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.nodir.commondto.entity.BaseEntity;
import uz.nodir.commondto.model.enums.ResponseStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity {
    @SequenceGenerator(name = "tran_seq",
            sequenceName = "tran_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "tran_seq",
            strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(name = "internal_id", unique = true)
    private String internalId;
    private String paymentId;
    private BigDecimal amount;
    private String account;
    @Enumerated(EnumType.STRING)
    private ResponseStatus status;
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
    private String rrn;

    public Transaction(String internalId, BigDecimal amount, String account, ResponseStatus status) {
        this.internalId = internalId;
        this.amount = amount;
        this.account = account;
        this.status = status;
    }
}
