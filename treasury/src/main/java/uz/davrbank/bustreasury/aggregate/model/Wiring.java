package uz.davrbank.bustreasury.aggregate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import uz.davrbank.bustreasury.util.CurrencyType;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "wiring",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_wiring_template_wiringId", columnNames = {"template_id", "wiring_id"})
        }
)
public class Wiring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CurrencyType type;

    @Column(name = "wiring_id", nullable = false)
    private Long wiringId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debit_mfo_id")
    private MFO debitMFO;

    @Column(name = "debit_account_num")
    private String debitAccountNum;

    @Column(name = "debit_inn")
    private String debitInn;

    @Column(name = "debit_comment")
    private String debitComment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_mfo_id")
    private MFO creditMFO;

    @Column(name = "credit_account_num")
    private String creditAccountNum;

    @Column(name = "credit_inn")
    private String creditInn;

    @Column(name = "credit_comment")
    private String creditComment;

    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purpose_id")
    private Purpose purpose;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doc_type_id")
    private DocType docType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typlate_id")
    private TypePlat typePlate;

    @Column(name = "created_by")
    private String createdBy;

    @Transient
    private String documentNumber;
}
