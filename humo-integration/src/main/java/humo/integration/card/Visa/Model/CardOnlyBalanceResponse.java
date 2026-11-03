package humo.integration.card.Visa.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardOnlyBalanceResponse {
    private Long balance;
    private Integer pinCount;
    private String holder;
    private Boolean status;
    private String phone;
}
