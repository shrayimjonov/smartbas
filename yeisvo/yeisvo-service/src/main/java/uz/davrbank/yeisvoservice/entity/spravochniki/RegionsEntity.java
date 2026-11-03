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
        name = "yeisvo_dic_region",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_id_number", columnNames = {"id_number"})
        }
)
public class RegionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "region_seq")
    @SequenceGenerator(
            name = "region_seq",
            sequenceName = "yeisvo_dic_region_seq",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "id_number", unique = true, nullable = false)
    private String idNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}


