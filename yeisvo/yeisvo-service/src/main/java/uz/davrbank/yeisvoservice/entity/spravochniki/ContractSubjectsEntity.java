package uz.davrbank.yeisvoservice.entity.spravochniki;

import lombok.*;

import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(
        name = "yeisvo_dic_contract_subjects",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cs_id", columnNames = {"id"})
        }
)
public class ContractSubjectsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_subjects_seq")
    @SequenceGenerator(
            name = "contract_subjects_seq",
            sequenceName = "yeisvo_dic_contract_subjects_seq",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "id_number")
    private Long idNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}

