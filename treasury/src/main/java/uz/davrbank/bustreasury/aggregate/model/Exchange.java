package uz.davrbank.bustreasury.aggregate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exchanges")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wiring_id", nullable = false)
    private Wiring wiring;
    @Column(name = "total", nullable = false)
    private Long total;
    @Column (name = "created_by", nullable = false)
    private String createdBy;
}
