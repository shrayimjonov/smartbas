package davr.yeisvo.entity.postgres;

import lombok.*;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "bank_branch", schema = "yeisvo")
public class BankBranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "inn", nullable = false, unique = true)
    private String inn;

    @Column(name = "mfo", nullable = false, unique = true)
    private String mfo;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

}
