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
        name = "yeisvo_dic_industrial_zone",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_id_number", columnNames = {"id_number"})
        }
)
public class IndustrialZoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "industrial_zone_seq")
    @SequenceGenerator(
            name = "industrial_zone_seq",
            sequenceName = "yeisvo_dic_industrial_zone_seq",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "id_number", unique = true, nullable = false)
    private Long idNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}

