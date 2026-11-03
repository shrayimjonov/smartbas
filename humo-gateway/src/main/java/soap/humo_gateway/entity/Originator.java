package soap.humo_gateway.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "originator",
        indexes = {
                @Index(name = "orig_bank_ind", columnList = "bank_name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "orig_bank_const", columnNames = "bank_name")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class Originator {
    @SequenceGenerator(name = "originator_seq",
            sequenceName = "originator_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "originator_seq",
            strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(name = "bank_name", length = 64)
    private String bankName;
    @Column(name = "originator", length = 32)
    private String originator;
}
