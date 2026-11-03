package davr.yeisvo.entity.postgres.spravochniki;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(
        name = "dic_bank_info",
        schema = "yeisvo",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
                @UniqueConstraint(columnNames = {"bankInn", "bankId"})
        }
)
public class BankInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankInn;
    private String bankId;
    private String bankClass;
    private String regionId;
    private Integer bankType;
    private String bankName;
    private String bankAddress;
    private String active;
    private String newBankInn;
    private String newBankId;
    private String parentNewBankInn;
    private String parentNewBankId;
}
