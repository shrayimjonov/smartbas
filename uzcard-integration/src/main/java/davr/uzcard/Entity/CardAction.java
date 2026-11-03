package davr.uzcard.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "card_action")
public class CardAction {
    @SequenceGenerator(name = "card_act_seq",
    sequenceName = "card_act_seq",
    allocationSize = 1)
    @GeneratedValue(generator = "card_act_seq",
    strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(name = "card", updatable = false)
    private String card;
    private String method;
    private String param1;
    private String param2;
    private String param3;
    private String additionalParam;
    private LocalDateTime createdTime;
    private Long logProtocolId;

}
