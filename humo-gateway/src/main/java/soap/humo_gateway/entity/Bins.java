package soap.humo_gateway.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@Entity
@Table(name = "humo_bins",
        indexes = {
                @Index(columnList = "binCode", name = "humo_bins_code")
        })
@AllArgsConstructor
@NoArgsConstructor
public class Bins {
    @SequenceGenerator(name = "hum_bin_seq",
            sequenceName = "hum_bin_seq",
            allocationSize = 1,
            initialValue = 214)
    @GeneratedValue(generator = "hum_bin_seq",
            strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "bank_c")
    private String bankCode;
    private String bankName;
    private String cardName;
    @Column(unique = true)
    private String binCode;
    private Boolean isPossible;
    private String centreId;

    public Bins(String bankCode, String bankName, String cardName, String binCode, Boolean isPossible, String centreId) {
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.cardName = cardName;
        this.binCode = binCode;
        this.isPossible = isPossible;
        this.centreId = centreId;
    }
}
