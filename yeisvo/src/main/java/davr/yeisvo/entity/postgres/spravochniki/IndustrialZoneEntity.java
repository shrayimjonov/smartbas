package davr.yeisvo.entity.postgres.spravochniki;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "dic_industrial_zone", schema ="yeisvo")
public class IndustrialZoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idNumber", unique = true, nullable = false)
    private Long idNumber;
    private String name;
    private Boolean isDeleted;

}
