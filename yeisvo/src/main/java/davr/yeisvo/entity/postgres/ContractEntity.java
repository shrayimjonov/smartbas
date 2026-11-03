package davr.yeisvo.entity.postgres;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table(name = "contract", schema = "yeisvo")
@TypeDef(name = "jsonb", typeClass = JsonType.class)
public class ContractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String idn;

    private String rootIdn;
    private String cntrType; //
    private String oldIdn;
    private String bankId; //
    private String bankInn;
    private String mvesNo;
    private String mvesDate;
    private String govDcsnNo;
    private String govDcsnDate;
    private String okpo; //
    private String okonx; //
    private String contractorUzParentCompany; //
    private String contractorUzName;  //
    private String rgnCode; //
    private Integer industrialZoneCode; //
    private String contractorUzAddress; //
    private String contractorUzPhone;
    private Integer cntrSubject; //
    private String currCode1; //
    private String currCode2; //
    private Double amount1; //
    private Double amount2; //
    private String accCurrCode1; //
    private String accCurrCode2;
    private String contractorForName; //
    private String contractorForCountryCode; //
    private String contractorForAddress; //
    private String bankForName; //
    private String bankForCountryCode;
    private String bankForAddress; //
    private String bankForAttributes; //
    private String centralBankLetterNo;
    private String centralBankLetterDate;
    private Integer creditRate; //
    private Integer leasingFlag; //
    private String productsName; //
    private String docNo; //
    private String docDate; //
    private Integer execType; //
    private String execDate;
    private String reestrRegNo;
    private String softwareName;
    private Integer cntrStatus; //
    private String lockDate;
    private int status; //
    private String cstDate;
    private String contractorUzInn;
    private Integer receivableTotal1; //
    private Integer receivableOverdue1;
    private Integer receivableTotal2;
    private Integer receivableOverdue2;
    private Integer payableTotal1;
    private Integer payableOverdue1;
    private Integer payableTotal2;
    private Integer payableOverdue2;
    private String insTime;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "data")
    private String data;

    @Column(name = "created_date")
    private LocalDateTime createdDate =  LocalDateTime.now();
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
