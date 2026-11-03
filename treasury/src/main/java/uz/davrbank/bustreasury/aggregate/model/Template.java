package uz.davrbank.bustreasury.aggregate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.davrbank.bustreasury.util.CurrencyType;
import uz.davrbank.bustreasury.util.OperationType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "template")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CurrencyType currency;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private OperationType operation;
}
