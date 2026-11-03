package uz.davrbank.yeisvoservice.entity.spravochniki;

import lombok.*;

import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(
        name = "yeisvo_dic_bank_info",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_bi_id", columnNames = {"id"}),
                @UniqueConstraint(name = "uk_bank_inn_bank_id", columnNames = {"bank_inn", "bank_id"})
        }
)
public class BankInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_info_seq")
    @SequenceGenerator( name = "bank_info_seq", sequenceName = "yeisvo_dic_bank_info_seq", allocationSize = 1 )
    @Column(name = "id")
    private Long id;
    @Column(name = "bank_inn")
    private String bankInn;
    @Column(name = "bank_id")
    private String bankId;
    @Column(name = "bank_class")
    private String bankClass;
    @Column(name = "region_id")
    private String regionId;
    @Column(name = "bank_type")
    private Integer bankType;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "bank_address")
    private String bankAddress;
    @Column(name = "active")
    private String active;
    @Column(name = "new_bank_inn")
    private String newBankInn;
    @Column(name = "new_bank_id")
    private String newBankId;
    @Column(name = "parent_new_bank_inn")
    private String parentNewBankInn;
    @Column(name = "parent_new_bank_id")
    private String parentNewBankId;
}
