package davr.yeisvo.entity.postgres.spravochniki;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "dic_country", schema ="yeisvo",   uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class DicCountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idNumber;
    private String name;
    private Boolean isDeleted;
}
