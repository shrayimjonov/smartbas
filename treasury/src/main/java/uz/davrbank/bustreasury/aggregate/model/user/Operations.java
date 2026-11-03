package uz.davrbank.bustreasury.aggregate.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "operations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Operations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String templateId;

    private LocalDateTime date;

    private String course;

    private String summa;

    @OneToMany(mappedBy = "operations", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SentWiring> sentWirings;


}
