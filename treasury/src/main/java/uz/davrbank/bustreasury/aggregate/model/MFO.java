package uz.davrbank.bustreasury.aggregate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mfo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MFO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;

    public MFO(Long id) {
        this.id = id;
    }
}