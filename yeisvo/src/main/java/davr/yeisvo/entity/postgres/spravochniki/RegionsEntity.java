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
        name = "dic_region",
        schema = "yeisvo",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
        }
)
public class RegionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idNumber", unique = true, nullable = false)
    private String idNumber;

    @Column(nullable = false)
    private String name;

    private Boolean isDeleted;
}

