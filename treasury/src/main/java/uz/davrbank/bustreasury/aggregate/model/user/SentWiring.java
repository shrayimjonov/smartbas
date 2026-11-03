package uz.davrbank.bustreasury.aggregate.model.user;

import jakarta.persistence.*;
import lombok.*;
import uz.davrbank.bustreasury.aggregate.model.Payment;
import uz.davrbank.bustreasury.aggregate.model.Wiring;

import java.time.LocalDateTime;

@Entity
@Table(name = "sent_wirings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SentWiring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String createdBy;
    private LocalDateTime createdDate;
    private String transId;

    private String errorCode;
    private String errorNote;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_id")
    private Operations operations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wiring_id")
    private Wiring wiring;

    @Column(name = "document_number") // Yangi maydon
    private String documentNumber;

    private String finaleState;

    private String finalizeAt;
}
