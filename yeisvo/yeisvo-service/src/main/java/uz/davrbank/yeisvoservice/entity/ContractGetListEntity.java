package uz.davrbank.yeisvoservice.entity;

import lombok.*;


import jakarta.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(
        name = "contract_get_list",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_contract_get_list_bank_idn",
                columnNames = {"bank_id", "idn"}
        )
)
public class ContractGetListEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_get_list_seq")
        @SequenceGenerator(
                name = "contract_get_list_seq",
                sequenceName = "contract_get_list_seq",
                allocationSize = 1
        )
        @Column(name = "id")
        private Long id;

        @Column(name = "idn")
        private String idn;

        @Column(name = "bank_inn")
        private String bankInn;

        @Column(name = "bank_id")
        private String bankId;

        @Column(name = "cntr_status")
        private Integer cntrStatus;

        @Column(name = "contractor_uz_name")
        private String contractorUzName;

        @Column(name = "doc_no")
        private String docNo;

        @Column(name = "doc_date")
        private String docDate;

        @Column(name = "contractor_for_name")
        private String contractorForName;

        @Column(name = "contractor_for_country_code")
        private String contractorForCountryCode;

        @Column(name = "contractor_uz_inn")
        private String contractorUzInn;

}


