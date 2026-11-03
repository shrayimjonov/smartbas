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
        name = "yeisvo_dic_country"
)
public class DicCountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dic_country_seq")
    @SequenceGenerator(
            name = "dic_country_seq",
            sequenceName = "yeisvo_dic_country_seq",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}

