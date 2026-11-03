package davr.uzcard.Entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "uzcard_params")
@AllArgsConstructor
@NoArgsConstructor
public class Params {
    @SequenceGenerator(name = "uzcard_param_seq",
            sequenceName = "uzcard_param_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "uzcard_param_seq",
            strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(unique = true)
    private String name;
    private String value;

    public Params(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
