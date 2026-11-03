package uz.davrbank.bustreasury.aggregate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;



@Entity
@Table(name = "doc_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "KOD_DOC")
    private String kodDoc;
    @Column(name = "NAIM_DOC")
    private String naimDoc;
    @Column(name = "DATE_OPEN")
    private LocalDateTime dateOpen;
    @Column(name = "DATE_CLOSE")
    private LocalDateTime dateClose;
    @Column(name = "ACT")
    private String act;

    public DocType(Long id) {
        this.id = id;
    }
}