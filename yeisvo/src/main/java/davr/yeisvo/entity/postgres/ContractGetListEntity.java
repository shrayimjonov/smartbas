package davr.yeisvo.entity.postgres;

import lombok.*;


import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "contract_get_list", schema ="yeisvo", uniqueConstraints = @UniqueConstraint(columnNames = {"bankId", "idn"}))
public class ContractGetListEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String idn;

        private String bankInn;

        private String bankId;

        private int cntrStatus;

        private String contractorUzName;

        private String docNo;

        private String docDate;

        private String contractorForName;

        private String contractorForCountryCode;

        private String contractorUzInn;


    }

