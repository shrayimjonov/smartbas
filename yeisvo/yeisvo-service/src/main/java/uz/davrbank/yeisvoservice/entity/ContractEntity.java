package uz.davrbank.yeisvoservice.entity;

import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table(
        name = "yeisvo_contract",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_idn",
                        columnNames = {"idn"}
                )
        }
)
public class ContractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_seq")
    @SequenceGenerator( name = "contract_seq", sequenceName = "YEISVO_CONTRACT_SEQ", allocationSize = 1 )
    @Column(name = "id")
    private Long id;

    @Column(name = "idn", unique = true)
    private String idn;

    @Column(name = "root_idn")
    private String rootIdn;

    @Column(name = "cntr_type")
    private String cntrType;

    @Column(name = "old_idn")
    private String oldIdn;

    @Column(name = "bank_id")
    private String bankId;

    @Column(name = "bank_inn")
    private String bankInn;

    @Column(name = "mves_no")
    private String mvesNo;

    @Column(name = "mves_date")
    private String mvesDate;

    @Column(name = "gov_dcsn_no")
    private String govDcsnNo;

    @Column(name = "gov_dcsn_date")
    private String govDcsnDate;

    @Column(name = "okpo")
    private String okpo;

    @Column(name = "okonx")
    private String okonx;

    @Column(name = "contractor_uz_parent_company")
    private String contractorUzParentCompany;

    @Column(name = "contractor_uz_name")
    private String contractorUzName;

    @Column(name = "rgn_code")
    private String rgnCode;

    @Column(name = "industrial_zone_code")
    private Integer industrialZoneCode;

    @Column(name = "contractor_uz_address")
    private String contractorUzAddress;

    @Column(name = "contractor_uz_phone")
    private String contractorUzPhone;

    @Column(name = "cntr_subject")
    private Integer cntrSubject;

    @Column(name = "curr_code_1")
    private String currCode1;

    @Column(name = "curr_code_2")
    private String currCode2;

    @Column(name = "amount_1", columnDefinition = "NUMBER(19,2)")
    private Double amount1;

    @Column(name = "amount_2", columnDefinition = "NUMBER(19,2)")
    private Double amount2;

    @Column(name = "acc_curr_code_1")
    private String accCurrCode1;

    @Column(name = "acc_curr_code_2")
    private String accCurrCode2;

    @Column(name = "contractor_for_name")
    private String contractorForName;

    @Column(name = "contractor_for_country_code")
    private String contractorForCountryCode;

    @Column(name = "contractor_for_address")
    private String contractorForAddress;

    @Column(name = "bank_for_name")
    private String bankForName;

    @Column(name = "bank_for_country_code")
    private String bankForCountryCode;

    @Column(name = "bank_for_address")
    private String bankForAddress;

    @Column(name = "bank_for_attributes")
    private String bankForAttributes;

    @Column(name = "central_bank_letter_no")
    private String centralBankLetterNo;

    @Column(name = "central_bank_letter_date")
    private String centralBankLetterDate;

    @Column(name = "credit_rate")
    private Integer creditRate;

    @Column(name = "leasing_flag")
    private Integer leasingFlag;

    @Column(name = "products_name")
    private String productsName;

    @Column(name = "doc_no")
    private String docNo;

    @Column(name = "doc_date")
    private String docDate;

    @Column(name = "exec_type")
    private Integer execType;

    @Column(name = "exec_date")
    private String execDate;

    @Column(name = "reestr_reg_no")
    private String reestrRegNo;

    @Column(name = "software_name")
    private String softwareName;

    @Column(name = "cntr_status")
    private Integer cntrStatus;

    @Column(name = "lock_date")
    private String lockDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "cst_date")
    private String cstDate;

    @Column(name = "contractor_uz_inn")
    private String contractorUzInn;

    @Column(name = "receivable_total_1")
    private Integer receivableTotal1;

    @Column(name = "receivable_overdue_1")
    private Integer receivableOverdue1;

    @Column(name = "receivable_total_2")
    private Integer receivableTotal2;

    @Column(name = "receivable_overdue_2")
    private Integer receivableOverdue2;

    @Column(name = "payable_total_1")
    private Integer payableTotal1;

    @Column(name = "payable_overdue_1")
    private Integer payableOverdue1;

    @Column(name = "payable_total_2")
    private Integer payableTotal2;

    @Column(name = "payable_overdue_2")
    private Integer payableOverdue2;

    @Column(name = "ins_time")
    private String insTime;

    @Column(columnDefinition = "CLOB", name = "data")
    private String data;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedDate;

}
