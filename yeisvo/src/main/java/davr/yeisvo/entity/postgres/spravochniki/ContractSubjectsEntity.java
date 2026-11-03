package davr.yeisvo.entity.postgres.spravochniki;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "dic_contract_subjects", schema ="yeisvo",   uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class ContractSubjectsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idNumber;
    private String name;
    private Boolean isDeleted;
}
